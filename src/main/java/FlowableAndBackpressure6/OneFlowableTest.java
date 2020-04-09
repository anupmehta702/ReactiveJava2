package FlowableAndBackpressure6;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

public class OneFlowableTest {
    public static void main(String[] args) throws InterruptedException {
        Flowable<Long> obsSrc = Flowable.interval(1, TimeUnit.SECONDS)
                .doOnNext(s-> System.out.println("Observerable sent val --> "+s))
                .subscribeOn(Schedulers.computation())
                ;

        obsSrc.subscribe(new Subscriber<Long>() {

            @Override
            public void onSubscribe(Subscription subscription) {
                subscription.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(Long value) {

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Printing value --> "+value +" by thread -->"+ Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        Thread.sleep(4000);
    }
}
