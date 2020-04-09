package Transformers8;

import io.reactivex.Observable;
import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class TwoCustomOperator {
    public static void main(String[] args) {
        Observable.empty().cast(String.class).lift(defaultValue("new Value !!")).subscribe(System.out::println);
    }
    public static ObservableOperator<String,String> defaultValue(String defaultValue){

        return new ObservableOperator<String,String>(){

            @Override
            public Observer apply(Observer observer) throws Exception {
                return new Observer<String>() {
                    boolean isEmpty = true;
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String o) {
                        isEmpty=false;
                        observer.onNext(defaultValue);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        if(isEmpty){
                            observer.onNext(defaultValue);
                        }else
                            observer.onComplete();
                    }
                };
            }
        };
    }
}
