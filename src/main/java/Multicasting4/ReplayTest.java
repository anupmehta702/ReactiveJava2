package Multicasting4;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class ReplayTest {
    public static void main(String[] args) throws InterruptedException {
        Observable<Long> source = Observable.interval(1, TimeUnit.SECONDS)
                .replay(2) //if no argument specified it will cache all values
                .autoConnect();
        Disposable sub1 = source.subscribe((v)-> System.out.println("Sub#1 val -->"+v));
        Thread.sleep(4000);// 0 1 2 3
        sub1.dispose();
        Disposable sub2 = source.subscribe((v)-> System.out.println("Sub#2 val -->"+v));
        Thread.sleep(4000); // 2 3 4 5 6 7
        // i.e. it replayed last two elements(2,3) first and then started emitting the latest
        sub2.dispose();
        Disposable sub3 = source.subscribe((v)-> System.out.println("Sub#3 val -->"+v));
        Thread.sleep(3000); // 6 7 8 9 (6,7) replayed last two values
        sub3.dispose();


        /*Output
        Sub#1 val -->0
        Sub#1 val -->1
        Sub#1 val -->2
        Sub#1 val -->3
        Sub#2 val -->2
        Sub#2 val -->3
        Sub#2 val -->4
        Sub#2 val -->5
        Sub#2 val -->6
        Sub#2 val -->7
         */



    }
}
