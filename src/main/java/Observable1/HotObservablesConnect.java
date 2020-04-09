package Observable1;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;

import java.util.Arrays;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;

public class HotObservablesConnect {
    public static void main(String[] args) throws InterruptedException {
        List<String> list = Arrays.asList("jack","jill","Rambo");
        Observable<Long> source = Observable.interval(1,SECONDS);//Observable.fromIterable(list);
        ConnectableObservable<Long> hotSource = source.publish();
        /*
        A connectable Observable resembles an ordinary Observable,except that it does not begin emitting items when it is subscribed to,
          but only when the Connect operator is applied to it.
          In this way you can wait for all intended observers to subscribe to the Observable before the Observable begins emitting items.
         */
        hotSource.subscribe(value-> System.out.print(" s1-> "+value),Throwable::printStackTrace,()-> System.out.println(" Completed"));
        Thread.sleep(2000);
        hotSource.subscribe(value-> System.out.print(" s2-> "+value),Throwable::printStackTrace,()-> System.out.println(" Completed"));
        hotSource.connect();
        //connect method holds the observable to not emit the values on subscribe,it holds the emission till connect is called
        Thread.sleep(4000);
    }
}
/*Output
s1-> 0 s1-> 1 s1-> 2 s2-> 2 s1-> 3 s2-> 3 s1-> 4 s2-> 4 s1-> 5 s2-> 5
*/
