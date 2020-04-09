package Observable1;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.subjects.PublishSubject;

import java.util.Arrays;
import java.util.List;

import static java.util.concurrent.TimeUnit.*;

public class HotObservables {
    public static void main(String[] args) throws InterruptedException {
        List<String> list = Arrays.asList("jack", "jill", "Rambo");
        Observable<String> source = Observable.fromIterable(list);
        ConnectableObservable<String> hotSource = source.publish();
        /*
        A connectable Observable resembles an ordinary Observable, except that it does not begin emitting items when it is subscribed to, but only when the Connect operator is applied to it. In this way you can wait
        for all intended observers to subscribe to the Observable before the Observable begins emitting items.
         */
        Disposable s1 = hotSource.subscribe(value -> System.out.print(" s1-> " + value), Throwable::printStackTrace, () -> System.out.println(" Completed"));
        Disposable s2 = hotSource.subscribe(value -> System.out.print(" s2-> " + value), Throwable::printStackTrace, () -> System.out.println(" Completed"));
        hotSource.connect();
        //connect method holds the observable to not emit the values on subscribe,it holds the emission till connect is called
        Thread.sleep(4000);
        //Note this is not multi threading,it is all happening in same thread
        s1.dispose();
        s2.dispose();

        System.out.println(" --- test using interval --");
        Observable<Long> intervalSource = Observable.interval(1, SECONDS);
        PublishSubject<Long> publishSubject = PublishSubject.create();
        /*
        Publis hSUbject is the only way I feel to create a hot observable ,else by default all Observables are cold by nature
         A Subject that emits (multicasts) items to currently subscribed Observers and terminal events to current
         or late Observers.that means it would emit current values to ongoing current subscribers,
         However subcribers that subscribe later wont get all the values but only those that are currently
         emitted and for subscribers that subscribe after all elements are emitted ,
         get only onComplete event
         If you dont use PublishSubject ,then the observer becomes cold observer and all the subscribers would
         receive values from the start
        */
        intervalSource.subscribe(publishSubject);

        publishSubject.subscribe((value) -> {
            System.out.println("Subscriber #1 onNext : " + value);
        });
        Thread.sleep(2000);
        //sleeping for 2 seconds ,which means below subscriber would not receive first 2 values from intervalSource
        publishSubject.subscribe((value) -> {
            System.out.println("Subscriber #2 onNext : " + value);
        });//it wont receive 0 ,1 ,2

        Thread.sleep(3000);
    }

    /*Output
 s1-> jack s2-> jack s1-> jill s2-> jill s1-> Rambo s2-> Rambo Completed
 Completed
  --- test using interval --
Subscriber #1 onNext : 0 // only 1 listens to this
Subscriber #1 onNext : 1 // only 1 listens to this
Subscriber #1 onNext : 2
Subscriber #2 onNext : 2
Subscriber #1 onNext : 3
Subscriber #2 onNext : 3
Subscriber #1 onNext : 4
Subscriber #2 onNext : 4

     */
}
