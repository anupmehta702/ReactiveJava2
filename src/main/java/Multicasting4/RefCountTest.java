package Multicasting4;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class RefCountTest {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("-- Using refcount --");
        Observable<Long> source4 = Observable.interval(1, TimeUnit.SECONDS)
                //.share();// same as .publish().refCount();
        .publish().refCount();
        /*share/refcount resets the value and ensures that all the subscribers receive value from the beginning
          What it does is ,it maintains the count of subscribers .
          Once the subscriber is disconnected it starts a new Observable and hence the subscriber after the
          intially subscriber is disconnected ,it receives value from the beginning .
          In the below example ,
          sub1 starts subscribing , refCount -1
          sub2 starts subscribing before sub1 stops/disposes ,refCount -2
          sub2 does not receive values fro mthe beginning .
          sub2 disposes ,sub1 disposes  , refCount -0
          sub3 starts subscribing ,refCount -1 and it receives value from the beginning
         */
        Disposable sub1 = source4.subscribe((v) -> System.out.println("Sub #1 val ->" + v));
        Thread.sleep(3000);
        Disposable sub2 = source4.subscribe((v) -> System.out.println("Sub #2 val ->" + v));
        Thread.sleep(3000);
        sub2.dispose();
        sub1.dispose();
        System.out.println("sub 1 & 2 disconnects");
        System.out.println("sub 3 connects with refCount being 0");
        Disposable sub3 = source4.subscribe((v) -> System.out.println("Sub #3 val ->" + v));
        Thread.sleep(4000);
        sub3.dispose();
        System.out.println("--Last subscriber disconnected -- ");
        Thread.sleep(2000);
        System.out.println("Exit !");
        /*
        -- Using refcount --
        Sub #1 val ->0
        Sub #1 val ->1
        Sub #1 val ->2
        Sub #1 val ->3
        Sub #2 val ->3
        Sub #1 val ->4
        Sub #2 val ->4
        Sub #1 val ->5
        Sub #2 val ->5
        sub 1 & 2 disconnects
        sub 3 connects with refCount being 0
        Sub #3 val ->0
        Sub #3 val ->1
        Sub #3 val ->2
        Sub #3 val ->3
        --Last subscriber disconnected --
        Exit !
        */
    }
}
