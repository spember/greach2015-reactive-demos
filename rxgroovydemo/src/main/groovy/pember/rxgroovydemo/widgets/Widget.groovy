package pember.rxgroovydemo.widgets

import groovy.transform.CompileStatic

/**
 * @author Steve Pember
 */
@CompileStatic
class Widget {
    WidgetType type
    int number = 0
    String sku
    int priceInCents = 0
    int inventory = 0
    int unitsSold = 0

}

@CompileStatic
enum WidgetType {
    // my class of widget
    FOO("foo"),
    BAR("bar"),
    SPROCKET("sprocket"),
    WHATZIT("whatzit")

    String id

    WidgetType(String i) {
        id = i
    }
}
