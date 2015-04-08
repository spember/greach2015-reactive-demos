package pember.rxgroovydemo.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pember.rxgroovydemo.widgets.SalesEvent
import pember.rxgroovydemo.widgets.Widget
import rx.Observable
import rx.schedulers.Schedulers

/**
 * @author Steve Pember
 */
@Service
class SalesService {

    private final Observable stream
    private final WidgetFactoryService widgetFactoryService


    @Autowired SalesService(WidgetFactoryService wfs) {
        widgetFactoryService = wfs

        stream = Observable.create({subscriber->
            Random r = new Random()
            try {
                while(true) {
                    Thread.sleep(r.nextInt(2500))
                    subscriber.onNext(new SalesEvent(quantity: r.nextInt(5)+1, widget: widgetFactoryService.widgets[r.nextInt(widgetFactoryService.widgets.size())]))
                }

            } catch(Exception e) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(e)
                }
            }
        })
    }

    Observable getSalesStream() {
        stream
        .onBackpressureBuffer()
        .observeOn(Schedulers.computation())
    }
}
