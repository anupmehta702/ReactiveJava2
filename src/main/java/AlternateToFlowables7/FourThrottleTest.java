package AlternateToFlowables7;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class FourThrottleTest {
    public static void main(String[] args) throws InterruptedException {
        Observable<String> s1 = Observable.interval(200, MILLISECONDS)
                .map(i -> (i + 1) * 200)
                .map(i -> "Source 1 :" + i)
                .take(50);

       /* Disposable is1 = s1.throttleLast(1, SECONDS).subscribe((v) -> System.out.println("Individual S1 -->" + v));
        Thread.sleep(4000);
        is1.dispose(); //prints 1000 1900 2900 3900
*/

        Observable<String> s2 = Observable.interval(300, MILLISECONDS)
                .map(i -> (i + 1) * 300)
                .map(i -> "Source 2 :" + i)
                .take(50);

        /*Disposable is2 = s2.throttleLast(2, SECONDS)
                .subscribe((v) -> System.out.println(" S1 S2 -->" + v));
        Thread.sleep(6000);
        is22.dispose(); //prints 900 1800 2700
*/

        Observable<String> s3 = Observable.interval(300, MILLISECONDS)
                .map(i -> (i + 1) * 300)
                .map(i -> "Source 3 :" + i)
                .take(10);

        System.out.println("-- Throttle first --");
        Disposable tFirst = Observable.concat(s1, s2, s3)
                .throttleFirst(1, SECONDS)
                .subscribe((v)->System.out.println("TF--> "+v));
        sleep(4000);
        tFirst.dispose();



        System.out.println("-- Throttle last --");
        Disposable tLast = Observable.concat(s1, s2, s3).throttleLast(1, SECONDS).subscribe(System.out::println);
        sleep(3000);
        tLast.dispose();




    }
}
/*Output
    -- Throttle first --
    TF--> Source 1 :100
    TF--> Source 2 :200
    TF--> Source 2 :1400
    TF--> Source 3 :600
    TF--> Source 3 :1800
    -- Throttle last --
    Source 1 :1000
    Source 2 :800
    Source 2 :1800
    Source 3 :900
    Source 3 :1800

 */