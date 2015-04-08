package pember.rxgroovydemo

import groovy.util.logging.Slf4j
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ApplicationContext
import rx.schedulers.Schedulers

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


//
//        def source = rx.Observable.create({subscriber->
//            try {
//
//                for (int i = 1; i < 1000; i++) {
//                    if (subscriber.isUnsubscribed()) {
//                        return;
//                    }
//                    subscriber.onNext(i)
//                }
//                if (!subscriber.isUnsubscribed()) {
//                    subscriber.onCompleted();
//                }
//            }catch(Exception e) {
//                if (!subscriber.isUnsubscribed()) {
//                    subscriber.onError(e)
//                }
//            }
//
//        })
//        println "About to start!"
//        source
//        .filter({it > 900})
//        .filter({int it -> it % 2 == 0})
//        .skip(10)
//        .take(5)
//        .reduce([],{acc, l ->
//            acc += l
//        })
//
//        .subscribe(
//                {log.info(it.toString())},
//                {log.error("Error!",it)},
//                {log.error("")})
//        println "Blah!"
    }
}
