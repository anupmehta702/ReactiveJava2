package TestAndDebug9;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;


public class BlockingSubscriberTest {
    public static int count = 0;

    @Test
    public void blockingSubscriberTest() {

        Observable<Long> source = Observable.interval(300, TimeUnit.MILLISECONDS).take(10);
        source.blockingSubscribe(i -> count++);

        assertTrue(count == 10);
    }

    @Test
    public void blockingOperatorTest() {

        Observable<Long> source = Observable.interval(300, TimeUnit.MILLISECONDS).take(10);
        //source.blockingSubscribe(i->count++);
        Long first = source.blockingFirst();
        Long last = source.blockingLast();

        assertTrue(first == 0);
        assertTrue(last == 9);
    }

    @Test
    public void blockingForEachOperatorTest() {
        Observable<Long> source = Observable.interval(300, TimeUnit.MILLISECONDS)
                .take(10)
                .filter(e -> e > 5);
        source.blockingForEach(i -> assertTrue(i > 5));
    }

}
