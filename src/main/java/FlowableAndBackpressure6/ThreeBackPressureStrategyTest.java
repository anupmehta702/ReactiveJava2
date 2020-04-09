package FlowableAndBackpressure6;

import io.reactivex.BackpressureOverflowStrategy;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ThreeBackPressureStrategyTest {
    public static void main(String[] args) throws InterruptedException {
        Disposable overflow = Flowable.range(1, 1_000_000)
                .onBackpressureBuffer(1, () -> System.out.println("Overflow"), BackpressureOverflowStrategy.ERROR) //keeps printing overflow in between
                //.onBackpressureLatest() //it prints 1 to 10 ,then directly prints 7670 till 7765 ,then prints 38932 and so on
                .onBackpressureDrop()//prints till 128 then starts with 21637 till 2173 ,then jumps to 51635 and so on
                .observeOn(Schedulers.computation())
                .subscribe(e -> {
                    System.out.println("Val ->" + e);
                }, Throwable::printStackTrace);
        Thread.sleep(10000);
        overflow.dispose();
    }

}

