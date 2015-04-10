package pember.akka.paddyspub.actors

import akka.actor.UntypedActor
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import pember.akka.paddyspub.messages.EmptyPint
import pember.akka.paddyspub.messages.FullPint
import pember.akka.paddyspub.messages.Order

/**
 * @author Steve Pember
 */
@Slf4j
@CompileStatic
class Bartender extends UntypedActor {
    // track the number of outstanding pint glasses
    int totalPints = 0

    @Override
    void onReceive(Object o) throws Exception {
        if (o instanceof Order) {
            onOrderReceive((Order)o)

        } else if (o instanceof EmptyPint) {
            onEmptyPintReceive((EmptyPint)o)
        }
    }

    void onOrderReceive(Order order) {
        log.info("I'll prepare ${order.getQuantity()} pints for ${sender.path().name()}")
        totalPints += order.getQuantity()
        // pour one pint per order
        order.getQuantity().times { i->
            log.info("Pouring pint ${i+1}")
            // pouring time
            Thread.sleep(1000)
            log.info("Pint ${i+1} is ready, here you go ${sender.path().name()}")
            sender.tell(new FullPint(i+1), self)
        }
    }

    void onEmptyPintReceive(EmptyPint pint) {
        totalPints--
        if (totalPints == 0) {
            log.info("Got all the pints back from those guys. Whew! What a busy day. Time to go home")
            context.system().shutdown()
        } else {
            log.info("Thanks for the pint ${sender.path().name()}, but there are ${totalPints} left")
        }
    }
}
