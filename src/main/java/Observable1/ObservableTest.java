package Observable1;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ObservableTest {

    public static void main(String[] args) {
        Observable<String> source = Observable.create((emitter) -> {
            try {
                emitter.onNext("First");
                emitter.onNext("Second");
                emitter.onNext("third");
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });

        Observer<String> sourceObserver = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                System.out.println("Subscription successful !");
            }

            @Override
            public void onNext(String s) {
                System.out.println("in OnNext method with value --> " + s);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error ->" + throwable.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("Observation is complete !");
            }
        };

        source.subscribe(sourceObserver);

        Observable<String> source2 = Observable.just("First", "Second", "Third");
        source2.subscribe((value) -> System.out.println("printing value -->" + value),
                Throwable::printStackTrace, () -> System.out.println("Observation is complete"));

    }
}
/*Output
Subscription successful !
in OnNext method with value --> First
in OnNext method with value --> Second
in OnNext method with value --> third
Observation is complete !
printing value -->First
printing value -->Second
printing value -->Third
Observation is complete

 */