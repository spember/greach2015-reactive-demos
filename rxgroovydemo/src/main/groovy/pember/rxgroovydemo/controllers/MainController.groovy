package pember.rxgroovydemo.controllers

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.context.request.async.DeferredResult
import pember.rxgroovydemo.services.WidgetFactoryService
import pember.rxgroovydemo.widgets.Widget
import pember.rxgroovydemo.widgets.WidgetType
import rx.schedulers.Schedulers

/**
 * @author Steve Pember
 */
@Slf4j
@Controller
class MainController {
    private final WidgetFactoryService widgetFactoryService

    @Autowired MainController(WidgetFactoryService wfs) {
        widgetFactoryService = wfs
    }

    @RequestMapping("/inventory")
    public @ResponseBody DeferredResult<Map<String, Object>> totalInventory() {
        DeferredResult<Map<String, Object>> deferredResult = new DeferredResult<Map<String, Object>>()

        widgetFactoryService.totalInventoryReport
        .subscribeOn(Schedulers.io())
        .map({Map report ->
            // add total inventory to the report
            report.totalInventory = report.values().sum()
            report
        })
        .subscribe({Map report -> deferredResult.setResult(report) })

        deferredResult
    }

    @RequestMapping("/inventory/{type}")
    public @ResponseBody DeferredResult<Map<String, Object>> specificInventory(@PathVariable(value="type") String type) {
        log.info("---- Attempting to filter by type {}", type)
        DeferredResult<Map<String, Object>> deferredResult = new DeferredResult<Map<String, Object>>()

        widgetFactoryService.observableWidgets
        .subscribeOn(Schedulers.io())
        .filter({Widget widget -> widget.type.id == type})
        .count()
        .subscribe({int total -> deferredResult.setResult([type: type, inventory: total])})

        deferredResult
    }

    @RequestMapping("/sales")
    public @ResponseBody DeferredResult<Map<String, Object>> salesBreakdown() {
        log.info("--- Generating Sales Report ---")
        DeferredResult<Map<String, Object>> deferredResult = new DeferredResult<Map<String, Object>>()

        widgetFactoryService.observableWidgets
        .groupBy({Widget widget -> widget.type.id})
        .flatMap({ group ->
            group
            .reduce([unitsSold: 0, totalCents:0], {total, widget ->
                total.unitsSold += widget.unitsSold
                total.totalCents += widget.unitsSold * widget.priceInCents
                total
            })
            .map({data ->
                data.usd = '$'+(data.totalCents/100)
                data
            })
            .map({total -> [(group.getKey()): total]})
        })
        .reduce([:], {acc, item -> acc += item})
        // note that I have the suscribeOn at the bottom of the chain here, but in other places it's at the top.
        // methods which have a meta effect on the stream can be placed anywhere, I believe
        .subscribeOn(Schedulers.io())
        .subscribe({deferredResult.setResult(it)})

        deferredResult
    }

}
