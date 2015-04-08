package pember.akka.paddyspub.actors

import akka.actor.UntypedActor
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import pember.akka.paddyspub.messages.EmptyPint
import pember.akka.paddyspub.messages.FullPint


/**
 * @author Steve Pember
 */
@Slf4j
@CompileStatic
class Drinker extends UntypedActor {
    @Override
    void onReceive(Object o) throws Exception {
        if (o instanceof FullPint) {
            onReceivePint((FullPint)o)
        } else {
            log.info("What is this ${o.class} you're trying to give me?")
        }
    }

    void onReceivePint(FullPint pint) {
        log.info("${self.path().name()}: Received pint ${pint.number}. Drinking it down")
        // drinking time
        Thread.sleep(1000)
        log.info("${self.path().name()}: That was delicious! Sending empty pint ${pint.getNumber()} back")
        sender().tell(new EmptyPint(pint.getNumber()), self)
    }
}
