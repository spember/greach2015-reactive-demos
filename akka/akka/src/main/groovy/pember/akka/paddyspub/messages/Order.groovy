package pember.akka.paddyspub.messages

import groovy.transform.Immutable

/**
 * @author Steve Pember
 */
@Immutable
class Order {
    // groovy's Immutable annotation is perfect in many cases

    int quantity

}
