package pember.rxgroovydemo.services

import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service
import pember.rxgroovydemo.widgets.Widget
import pember.rxgroovydemo.widgets.WidgetType
import rx.Observable
import rx.schedulers.Schedulers

import javax.annotation.PostConstruct

/**
 * @author Steve Pember
 */
@Slf4j
@Service
class WidgetFactoryService {

    List<Widget> widgets = []

    @PostConstruct
    void init() {
        log.info("Generating Factory Inventory")
        Random r = new Random()
        1000.times {
            WidgetType type = WidgetType.values()[r.nextInt(WidgetType.values().size())]
            Widget widget = new Widget(
                    type: type,
                    number: it,
                    sku: "${type.id}-${it}",
                    priceInCents: r.nextInt(20000),
                    inventory: r.nextInt(999)+1,
                    unitsSold: 0
            )
            widgets << widget

        }
        log.info("Widgets generated; counting inventory")

        getTotalInventoryReport()
        .subscribeOn(Schedulers.computation())
        .subscribe({report->
            log.info("We have type counts totals of:")
            report.each {k, v-> log.info("* $k: $v")}
        })
        log.info("Exiting")

    }

    /**
     *
     *
     * @return An Observable emitting a single Map summary of the Inventory
     */
    Observable getTotalInventoryReport() {
        //Observable.from(widgets)
        getObservableWidgets()
            .groupBy({Widget w -> w.type})
            .flatMap({group ->
                group
                .count()
                .map({total -> [(group.getKey()): total] })
            })
            .reduce([:], {Map acc, Map item-> acc += item })
    }

    Observable getObservableWidgets() {
        Observable.from(widgets)
    }


    // potentially add a hot observable that routinely emits events surrounding Widget sales

}


