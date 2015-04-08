package pember.akka

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import groovy.util.logging.Slf4j
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ApplicationContext
import pember.akka.paddyspub.actors.Bartender
import pember.akka.paddyspub.actors.Drinker
import pember.akka.paddyspub.messages.Order

/**
 * @author Steve Pember
 */
@Slf4j
@SpringBootApplication(exclude=DataSourceAutoConfiguration)
class Application {
    static void main(String[] args) {
        ApplicationContext ctx = new SpringApplicationBuilder(Application)
                .showBanner(false)
                .run(args)

        log.info("\n*************************\n*\n*\tWelcome to the Pub!\n*\n*************************")
        final ActorSystem system = ActorSystem.create("actor-system")
        Thread.sleep(500)
        // create all of our Actors within our Actor system
        // Props tells the actor system how to build the particular actor
        final ActorRef dee = system.actorOf(Props.create(Bartender.class), "Sweet_Dee")
        final ActorRef charlie = system.actorOf(Props.create(Drinker.class), "Charlie")
        final ActorRef mac = system.actorOf(Props.create(Drinker.class), "Mac")
        final ActorRef dennis = system.actorOf(Props.create(Drinker.class), "Dennis")
        final ActorRef frank = system.actorOf(Props.create(Drinker.class), "Frank")


        dee.tell(new Order(quantity: 5), charlie)

        dee.tell(new Order(quantity: 3), mac)

        dee.tell(new Order(quantity: 4), dennis)

        dee.tell(new Order(quantity: 7), frank)



        system.awaitTermination()
    }
}
