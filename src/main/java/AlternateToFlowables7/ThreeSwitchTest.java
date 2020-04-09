package AlternateToFlowables7;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class ThreeSwitchTest {
    public static void main(String[] args) throws InterruptedException {
        Observable<String> s1 = Observable.interval(300, TimeUnit.MILLISECONDS).map(v->v+" switched to the first Observable ");
        Disposable s2 = Observable.interval(1, TimeUnit.SECONDS)
                .switchMap(i -> s1.doOnDispose(() -> System.out.println("First Observable is being disposed of ")))
                .subscribe(System.out::println);//this is never printed ,major differnce between switch operator and switchMap
        Thread.sleep(5000);
    }
    /*Output
    0 switched to the first Observable
    1 switched to the first Observable
    2 switched to the first Observable
    First Observable is being disposed of //at this time the s2 emits the value ,it resets s1 and starts emitting value
    0 switched to the first Observable
    1 switched to the first Observable
    2 switched to the first Observable
    First Observable is being disposed of
    0 switched to the first Observable
    1 switched to the first Observable
    2 switched to the first Observable
    First Observable is being disposed of
    0 switched to the first Observable
    1 switched to the first Observable
    2 switched to the first Observable
    First Observable is being disposed of

     */
}
