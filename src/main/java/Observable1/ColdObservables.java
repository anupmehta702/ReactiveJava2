package Observable1;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ColdObservables {
    public static void main(String[] args) throws InterruptedException {
        Observable<String> source = Observable.just("first", "second", "third");
        source.subscribe(value -> System.out.print(" " + value), Throwable::printStackTrace, () -> System.out.println(" Completed"));
        source.subscribe(value -> System.out.print(" " + value), Throwable::printStackTrace, () -> System.out.println(" Completed"));

        System.out.println("--Using Iterable--");
        List<String> list = Arrays.asList("jack", "jill", "Rambo");
        Observable<String> source2 = Observable.fromIterable(list);
        source2.subscribe(value -> System.out.print(" " + value), Throwable::printStackTrace, () -> System.out.println(" Completed"));


        System.out.println("-- Using range -- ");
        Observable<Integer> rangeSource = Observable.range(1, 4);
        rangeSource.subscribe(v -> System.out.print(" Range value -> " + v),
                Throwable::printStackTrace,
                () -> System.out.println(" Range Completed"));

        System.out.println(" -- test using intervals --");
        Observable<Long> intervalSource = Observable.interval(1, SECONDS);

        intervalSource.subscribe((value) -> System.out.println("Subscriber #1 onNext : " + value),
                Throwable::printStackTrace,
                () -> System.out.println(" Interval Completed for Subscriber #1"));

        Thread.sleep(2000);
        //sleeping for 2 seconds ,despite this below subscriber would listen to all values emitted before it subscribed

        intervalSource.subscribe((value) -> {
            System.out.println("Subscriber #2 onNext : " + value);
        });

        Thread.sleep(4000);//this sleep is required bcoz our main thread exists before it could emit the values


    }
}
/*output
 first second third Completed
 first second third Completed
--Using Iterable--
 jack jill Rambo Completed
 -- Using range --
 Range value -> 1 Range value -> 2 Range value -> 3 Range value -> 4Range complete
 -- test using intervals --
Subscriber #1 onNext : 0
Subscriber #1 onNext : 1
Subscriber #1 onNext : 2
Subscriber #2 onNext : 0 //subscriber 2 still listens to this despite the fact that it was emitted before it was subscribed
Subscriber #1 onNext : 3
Subscriber #2 onNext : 1
Subscriber #1 onNext : 4
Subscriber #2 onNext : 2
Subscriber #2 onNext : 3
Subscriber #1 onNext : 5



 */