package Multicasting4;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class ReplayAndCacheTest {
    public static void main(String[] args) throws InterruptedException {
        //Note - I have not understood it yet ,need to research on cache vs replay
        Observable<Long> source = Observable.interval(1, TimeUnit.SECONDS)
                .cacheWithInitialCapacity(2) //if no argument specified it will cache all values
                .publish()
                .autoConnect();
        /*when you use replay, it will hold emissions until the new observer gets them and then
        it will dispose of itself. this is good for memory management,
        but if you want to hold all emissions indefinitly,
        then you can use cache() operator which does the same thing as replay() but persists on emissions.

         */
        Disposable sub1 = source.subscribe((v)-> System.out.println("Sub#1 val -->"+v));
        Thread.sleep(4000);// 0 1 2 3
        //sub1.dispose();
        Disposable sub2 = source.subscribe((v)-> System.out.println("Sub#2 val -->"+v));
        Thread.sleep(4000); // 4 5 6 7
        sub1.dispose();
        sub2.dispose();
        /*Disposable sub3 = source.subscribe((v)-> System.out.println("Sub#3 val -->"+v));
        Thread.sleep(4000); // 8 9 10 11
        sub3.dispose();
*/    }
}
