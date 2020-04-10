package TestAndDebug9;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TwoObserverTest {
    @Test
    public void observerTest(){
        Observable<Integer> source = Observable.range(1,10);

        TestObserver<Integer> testObserver = new TestObserver<>();
        testObserver.assertNotSubscribed();
        source.subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        testObserver.assertComplete();
        testObserver.assertNoErrors();
        testObserver.assertValueCount(10);
        testObserver.assertValues(1,2,3,4,5,6,7,8,9,10);
    }

    @Test
    public void timeSchedulerTest(){
        TestScheduler ts = new TestScheduler();
        Observable<Long> source =Observable.interval(1, TimeUnit.MINUTES,ts);
        TestObserver<Long> testSub = new TestObserver<>();
        source.subscribe(testSub);

        ts.advanceTimeBy(30,TimeUnit.SECONDS);
        testSub.assertValueCount(0);

        ts.advanceTimeTo(90,TimeUnit.MINUTES);
        testSub.assertValueCount(90);

        ts.advanceTimeBy(65,TimeUnit.SECONDS);
        testSub.assertValueCount(91);
    }
}
