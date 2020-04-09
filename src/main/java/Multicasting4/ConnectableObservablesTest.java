package Multicasting4;


import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;

import java.util.concurrent.TimeUnit;

public class ConnectableObservablesTest {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("-- Using connect --");
        ConnectableObservable<Integer> source = Observable.range(1, 3).map(i -> i + 2).publish();
        /*ConnectableObservable are special observables which are cold observables and emit vlaues only when
        connect is called and not when subscribers subscribe.
        publish method is used to convert a simple observable to completableObservables
        */
        Disposable sub1 = source.subscribe((v) -> System.out.println("Sub #1 val ->" + v));
        Disposable sub2 = source.subscribe((v) -> System.out.println("Sub #2 val ->" + v));
        source.connect();
        sub1.dispose();
        sub2.dispose();
        /*Output
        -- Using connect --
        Sub #1 val ->3
        Sub #2 val ->3
        Sub #1 val ->4
        Sub #2 val ->4
        Sub #1 val ->5
        Sub #2 val ->5
         */

        System.out.println("-- Using autoconnect(number) --");
        Observable<Integer> source1 = Observable.range(1, 3)
                .map(i -> i + 2)
                .publish().autoConnect(2);
        //use this only if you know the number of observers else rely on connect
        Disposable sub11 = source1.subscribe((v) -> System.out.println("Sub #11 val ->" + v));
        Disposable sub22 = source1.subscribe((v) -> System.out.println("Sub #22 val ->" + v));
        sub11.dispose();
        sub22.dispose();
        /*Output
        -- Using autoconnect(number) --
        Sub #11 val ->3
        Sub #22 val ->3
        Sub #11 val ->4
        Sub #22 val ->4
        Sub #11 val ->5
        Sub #22 val ->5
         */


        System.out.println("-- Using autoconnect() --");
        Observable<Long> source3 = Observable.interval(1, TimeUnit.SECONDS)
                .publish().autoConnect();
        /*
        this gives unknown behavior ,avoid using autoconnect with no input.
        as it assumes only one subscriber is connected ,so for other subscribers it does not emit values
        from the beginning
        sub32 and sub 33 do not get the values 0,1
         */
        Disposable sub31 = source3.subscribe((v) -> System.out.println("Sub #31 val ->" + v));
        Thread.sleep(2000);
        Disposable sub32 = source3.subscribe((v) -> System.out.println("Sub #32 val ->" + v));
        Thread.sleep(4000);
        Disposable sub33 = source3.subscribe((v) -> System.out.println("Sub #33 val ->" + v));
        Thread.sleep(2000);
        sub31.dispose();
        sub32.dispose();
        sub33.dispose();
        System.out.println("------END ---------");
        Thread.sleep(2000);
        System.out.println("Exit !");
        /*
        -- Using autoconnect() --
        Sub #31 val ->0
        Sub #31 val ->1
        Sub #32 val ->1
        Sub #31 val ->2
        Sub #32 val ->2
        Sub #31 val ->3
        Sub #32 val ->3
        Sub #31 val ->4
        Sub #32 val ->4
        Sub #31 val ->5
        Sub #32 val ->5
        Sub #31 val ->6
        Sub #32 val ->6
        Sub #33 val ->6
        Sub #31 val ->7
        Sub #32 val ->7
        Sub #33 val ->7
        ------END ---------
        Exit !
 */

    }
}
