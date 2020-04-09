package FlowableAndBackpressure6;

import io.reactivex.BackpressureOverflowStrategy;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TwoBackPressureStrategyTest {
    public static void main(String[] args) throws InterruptedException {
        //The below code does not work
        Flowable<Integer> flw = Flowable.range(1, 1_000_000)
                //.onBackpressureLatest()
                .onBackpressureBuffer(1, () -> System.out.println("Overflow"), BackpressureOverflowStrategy.DROP_LATEST)
                .observeOn(Schedulers.computation());
        Disposable sub1 = flw.subscribe(v -> {
            System.out.println("sub#1 Printing val -->" + v);
        }, Throwable::printStackTrace);
        Thread.sleep(20000);
        sub1.dispose();
        System.out.println("End of program !!");



        List testList = IntStream.range(0, 100000)
                .boxed() //converts int to integer ,since collections are always of Objects and not primitives
                .collect(Collectors.toList());

        testBackPressure(testList, BackpressureStrategy.DROP);
        testBackPressure(testList, BackpressureStrategy.BUFFER);
        testBackPressure(testList, BackpressureStrategy.LATEST);
        testBackPressure(testList, BackpressureStrategy.MISSING);
        testBackPressure(testList, BackpressureStrategy.ERROR);


    }


    public static void testBackPressure(List testList, BackpressureStrategy bps) {

        Observable observable = Observable.fromIterable(testList);
        TestSubscriber<Integer> testSubscriber = observable
                .toFlowable(bps)
                .observeOn(Schedulers.computation())
                .test();
        testSubscriber.awaitTerminalEvent();
        if (bps == BackpressureStrategy.ERROR || bps == BackpressureStrategy.MISSING) {
            System.out.println("Asserting error for " + bps.toString());
            testSubscriber.assertError(MissingBackpressureException.class);
        } else {
            List<Integer> receivedInts = testSubscriber.getEvents()
                    .get(0)
                    .stream()
                    .mapToInt(object -> (int) object)
                    .boxed()
                    .collect(Collectors.toList());
            System.out.println(bps.toString() + "--> Size of received ints -->" + receivedInts.size());

        }
    }

}
/*Output
DROP--> Size of received ints -->5696
BUFFER--> Size of received ints -->100000
LATEST--> Size of received ints -->129
Asserting error for MISSING
Asserting error for ERROR
*/