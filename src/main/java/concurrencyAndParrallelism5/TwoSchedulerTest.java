package concurrencyAndParrallelism5;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TwoSchedulerTest {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        Scheduler sch = Schedulers.from(service);
        Observable<String> source = Observable.just("white","red","green")//,"blue","maroon","black","golden")
                //.subscribeOn(sch);
                .subscribeOn(Schedulers.computation());
        source.subscribe((v)-> System.out.println("Sub#1 val -->"+v+" time --> "+ LocalTime.now()
                +" thread-->"+Thread.currentThread().getName()));
        source.subscribe((v)-> System.out.println("Sub#2 val -->"+v+" time --> "+ LocalTime.now()
                +" thread-->"+Thread.currentThread().getName()));
        Thread.sleep(3000);
        Schedulers.shutdown();

    }
}
/*Output
    Sub#1 val -->white time --> 18:57:36.214 thread-->RxComputationThreadPool-1
    Sub#2 val -->white time --> 18:57:36.214 thread-->RxComputationThreadPool-2
    Sub#1 val -->red time --> 18:57:36.214 thread-->RxComputationThreadPool-1
    Sub#2 val -->red time --> 18:57:36.214 thread-->RxComputationThreadPool-2
    Sub#1 val -->green time --> 18:57:36.214 thread-->RxComputationThreadPool-1
    Sub#2 val -->green time --> 18:57:36.214 thread-->RxComputationThreadPool-2

 */
