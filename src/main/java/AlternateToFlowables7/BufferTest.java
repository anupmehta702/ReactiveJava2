package AlternateToFlowables7;


import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class BufferTest {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> finalList = new ArrayList<>();
        Disposable sub = Observable.range(1, 20)
                .buffer(10) //returns  Observable<List<Integer>>
                /*The below flatMap converts the Observable<List<Integer>> to Observable<Integer>
                .flatMap(e->{finalList.addAll(e);
                    Observable<Integer> observable = Observable.fromIterable(finalList);
                    return observable;})
                    */
                .subscribe((v) -> {
                    System.out.println("val -->" + v);
                });
        sub.dispose();
        /*Output
        val -->[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        val -->[11, 12, 13, 14, 15, 16, 17, 18, 19, 20]
         */

        Disposable sub2 = Observable.interval(200, MILLISECONDS)
                .buffer(1, SECONDS)
                .subscribe((v) -> {
                    System.out.println("Interval val -->" + v);
                });
        Thread.sleep(4000);
        sub2.dispose();
        /*Output  the size is not fixed
        Interval val -->[0, 1, 2, 3]
        Interval val -->[4, 5, 6, 7, 8, 9]
        Interval val -->[10, 11, 12, 13, 14]
         */

        Disposable sub3 = Observable.range(1,20)
                .buffer(5,6) //skips 6th element --> 6,12,18
                .subscribe((v) -> {
                    System.out.println("Skip val -->" + v);
                });
        sub3.dispose();
        /*Output
        skip val -->[1, 2, 3, 4, 5]
        skip val -->[7, 8, 9, 10, 11]
        skip val -->[13, 14, 15, 16, 17]
        skip val -->[19, 20]
         */


        System.out.println("-- Skip less than the buffer count --");
        Disposable sub4 = Observable.range(1,10)
                .buffer(5,1) // i.e. skip less than the count
                //it remits last 4 (5-1) elements every time from previous list
                // [1,2,3,4,5] , [2,3,4,5,6]
                .subscribe((v) -> {
                    System.out.println("Skip val -->" + v);
                });
        sub4.dispose();
        /*Output
        Skip val -->[1, 2, 3, 4, 5]
        Skip val -->[2, 3, 4, 5, 6]
        Skip val -->[3, 4, 5, 6, 7]
        Skip val -->[4, 5, 6, 7, 8]
        Skip val -->[5, 6, 7, 8, 9]
        Skip val -->[6, 7, 8, 9, 10]
        Skip val -->[8, 9, 10]
        Skip val -->[9, 10]
        Skip val -->[10]

         */


        System.out.println("-- boundary based buffer --");
        Disposable sub5 = Observable.interval(200, MILLISECONDS)
                .buffer(Observable.interval(1, SECONDS)) //creates boundary of 1 sec interval
                //and after one second passes the values to below subscriber ,very similiar to time based buffer
                .subscribe((v) -> {
                    System.out.println("Boundary based val -->" + v);
                });
        Thread.sleep(5000);
        sub5.dispose();
        /*
        -- boundary based buffer --
    Boundary based val -->[0, 1, 2, 3, 4]
    Boundary based val -->[5, 6, 7, 8, 9]
    Boundary based val -->[10, 11, 12, 13]
    Boundary based val -->[14, 15, 16, 17, 18, 19]
    Boundary based val -->[20, 21, 22, 23]
         */

    }
}
