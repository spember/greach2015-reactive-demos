package pember.rxgroovydemo.services

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pember.rxgroovydemo.widgets.SalesEvent
import pember.rxgroovydemo.widgets.WidgetType
import rx.schedulers.Schedulers

import javax.annotation.PostConstruct

/**
 * @author Steve Pember
 */
@Slf4j
@Service
class ReportService {

    private final SalesService salesService

    @Autowired ReportService(SalesService ss) {
        salesService = ss
    }

    @PostConstruct
    void watchSales() {
        log.info("Watching sales...")
        salesService.salesStream
        .subscribeOn(Schedulers.computation())
        .subscribe({SalesEvent event->
            log.info("sku ${event.widget.sku} just sold ${event.quantity} units! We just made ${event.widget.priceInCents * event.quantity} cents!")
        })


        // also, let's suppose we really want to watch for Sprocket sales

//        salesService.salesStream
//        .subscribeOn(Schedulers.computation())
//        .filter({SalesEvent event-> event.widget.type == WidgetType.SPROCKET})
//        .subscribe({SalesEvent event ->
//            log.info("Whoop! Just sold some sprockets!")
//        })
    }
}
