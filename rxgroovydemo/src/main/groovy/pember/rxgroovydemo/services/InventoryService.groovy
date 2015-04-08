package pember.rxgroovydemo.services

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pember.rxgroovydemo.widgets.SalesEvent
import rx.Observable
import rx.schedulers.Schedulers

import javax.annotation.PostConstruct

/**
 * @author Steve Pember
 */
@Slf4j
@Service
class InventoryService {

    private final SalesService salesService

    @Autowired InventoryService(SalesService ss) {
        salesService = ss
    }

    @PostConstruct
    private void watchStream() {
        // I deduct inventory!

        // set the base observable to branch off of
        Observable branch = salesService.salesStream
        .subscribeOn(Schedulers.computation())

        // branch one... filter results and buffer into chunks for persistence
        branch
        .filter({SalesEvent event -> event.widget.inventory >= event.quantity})
        //.subscribeOn(Schedulers.newThread())
        .buffer(10)
        .subscribe({List<SalesEvent> events ->
            log.info("Recording sales")
            // pretend this is a batch insert...
            for(SalesEvent event: events) {
                event.widget.unitsSold+=event.quantity
                event.widget.inventory-=event.quantity
            }

        })

        // branch two... filter on a different metric and, for now, log the events
        branch
        .filter({SalesEvent event -> event.widget.inventory < event.quantity})
        .subscribe({SalesEvent event -> log.error(
                "We're out of stock for sku ${event.widget.sku}!")})

    }
}
