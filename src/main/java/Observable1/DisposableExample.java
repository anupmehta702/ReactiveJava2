package Observable1;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class DisposableExample {

    public static void main(String[] args) throws InterruptedException {
        Observable<Long> source = Observable.interval(1, TimeUnit.SECONDS);
        Disposable disp = source.subscribe(System.out::println);
        Thread.sleep(5000);
        disp.dispose();
        Thread.sleep(4000);

        System.out.println("--Composite Disposable -- ");
        Observable<Long> source1 = Observable.interval(1, TimeUnit.SECONDS);
        Disposable disp1 = source1.subscribe(System.out::println);
        Disposable disp2 = source1.subscribe(System.out::println);
        Thread.sleep(4000);
        CompositeDisposable disposables = new CompositeDisposable();
        disposables.addAll(disp1,disp2);
        disposables.dispose();


        Observer<Long> obs = new Observer<Long>() {
            Disposable dis;
            @Override
            public void onSubscribe(Disposable disposable) {
                this.dis = disposable;
                System.out.println("In onSubscribe method !");
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println("In onNext method for value - "+aLong);
            }

            @Override
            public void onError(Throwable throwable) {
                dis.dispose();
            }

            @Override
            public void onComplete() {
                //disposable happens automatically
                System.out.println("In onComplete method !");
                dis.dispose();
            }
        };
        source.subscribe(obs);
        Thread.sleep(5000);

    }
}
