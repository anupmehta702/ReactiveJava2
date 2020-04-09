package concurrencyAndParrallelism5;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class OneBlockingSubscriptionTest {
    public static void main(String[] args) {
        Observable<Long> source = Observable.interval(1, TimeUnit.SECONDS);
        /*
        blockingSubscribe blocks the main thread till the onComplete is called for subscriber
        THis is majorily used for testing purpose only .
         */
        source.take(3).blockingSubscribe((v)-> System.out.println("Sub#1 val ->"+v),Throwable::printStackTrace,()-> System.out.println("on complete !"));
        source.take(3).blockingSubscribe((v)-> System.out.println("Sub#2 val ->"+v),Throwable::printStackTrace,()-> System.out.println("on complete !"));
    }
    /*Output
    Sub#1 val ->0
    Sub#1 val ->1
    Sub#1 val ->2
    on complete !
    Sub#2 val ->0
    Sub#2 val ->1
    Sub#2 val ->2
    on complete !
     */

}
