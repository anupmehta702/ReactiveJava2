package TestAndDebug9;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import org.junit.Test;

public class DebugObservableTest {
    @Test
    public void debugObservable(){
        Single<String> single = Single.just("12ze4-Anup-87345e-Mehta-1qw345-Pune-19bf59");
        TestObserver<String> to = new TestObserver<String>();
        single.flatMapObservable((s)->Observable.fromArray(s.split("-")))
                .doOnNext((v)-> System.out.println("on Next of each obs ->"+v))
                //keep using doOnNext to check values at each stage
                .filter(v->v.matches("[A-Za-z]+"))
                .doOnNext((v)-> System.out.println("After filtering values ->"+v))
                .subscribe(to);
        to.assertValues("Anup","Mehta","Pune");
    }
}
/*
Output
on Next of each obs ->12ze4
on Next of each obs ->Anup
After filtering values ->Anup
on Next of each obs ->87345e
on Next of each obs ->Mehta
After filtering values ->Mehta
on Next of each obs ->1qw345
on Next of each obs ->Pune
After filtering values ->Pune
on Next of each obs ->19bf59
 */