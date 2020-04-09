package Transformers8;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;

public class OneObservableTransformers {
    public static void main(String[] args) {
        Disposable s1 = Observable.just("one", "two", "three", "four", "five")
                .compose(mapStringToNumber())
                .subscribe((v) -> System.out.println("s1->" + v));
        Disposable s2 = Observable.just("red", "maroon", "green", "yellow", "blue")
                .compose(mapStringToNumber())
                .subscribe((v) -> System.out.println("s2->" + v));
    }

    public static ObservableTransformer<String, Integer> mapStringToNumber() {
        return myStream -> myStream.map(i -> i.length()).filter(i -> i > 3);
        //OR
        /*return new ObservableTransformer<String, Integer>() {
            @Override
            public ObservableSource<Integer> apply(Observable<String> myObservable) {
                return myObservable.map(i -> i.length()).filter(i -> i > 3);
            }
        };*/


    }
}
