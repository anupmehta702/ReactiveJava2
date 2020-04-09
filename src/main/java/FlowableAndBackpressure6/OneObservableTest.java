package FlowableAndBackpressure6;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OneObservableTest {
    public static void main(String[] args) throws InterruptedException {
        /*Observable<Long> obsSrc = Observable.rangeLong(1,5000000)//interval(1, TimeUnit.SECONDS)
                .doOnNext(s-> System.out.println("Observerable sent val --> "+s))
                .subscribeOn(Schedulers.computation());
        *//*PublishSubject<Long> publishSubject = PublishSubject.create();
        obsSrc.subscribe(publishSubject);*//*
        obsSrc.subscribe(new Observer<Long>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long value) {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Printing value --> "+value +" by thread -->"+ Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
*/


        PublishSubject<Integer> source = PublishSubject.<Integer>create();

        source.observeOn(Schedulers.computation())
                .subscribe(ComputeFunction::compute, Throwable::printStackTrace);

        IntStream.range(1, 1_000_000).forEach(source::onNext);
        while(true){

        }
        //test();
    }

    public static void test(){
        List testList = IntStream.range(0, 100000)
                .boxed()
                .collect(Collectors.toList());
        Observable<Integer> observable = Observable.fromIterable(testList);

        TestObserver<Integer> testSubscriber = observable
                .observeOn(Schedulers.computation())
                //.subscribe(ComputeFunction::compute, Throwable::printStackTrace)
                .test();
        testSubscriber.awaitTerminalEvent();
        List<Integer> receivedInts = testSubscriber.getEvents()
                .get(0)
                .stream()
                .mapToInt(object -> (int) object)
                .boxed()
                .collect(Collectors.toList());
        System.out.println(" Size of received ints -->" + receivedInts.size());


    }
}
 class ComputeFunction {
    public static void compute(Integer v) {
        try {
            System.out.println("compute integer v: " + v);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

