package combineObservables3;

import io.reactivex.Observable;
import io.reactivex.observables.GroupedObservable;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class CombineObservablesTest {
    public static void main(String[] args) throws InterruptedException {
        Observable<String> source = Observable.just("alpha");
        Observable<String> source2 = Observable.just("beta");
        Observable<String> source3 = Observable.just("gamma");
        Observable.merge(source, source2, source3).subscribe(System.out::println);//alpha beta gamma

        source.mergeWith(source3).subscribe(System.out::println);//alpha gamma

        source.flatMap(s -> Observable.fromArray(s.toString().split(""))).subscribe(System.out::println);
        //a l p h a

        source2.concatWith(source3).subscribe(System.out::println);//beta gamma

        Observable<String> source6 = Observable.just("alpha", "beta", "gamma");
        Observable<String> source7 = Observable.just("theta", "pita");

        Observable.zip(source6, source7, (s1, s2) -> s1 + " " + s2).subscribe(System.out::println);
        /*Zip - it combines results of both sources together .
        Note gamma didnt have any eqvivalent record in source7 so it ignores the gamma value
         alpha theta
         beta pita */


        Observable<Long> source8 = Observable.interval(1, SECONDS);
        Observable<Long> source9 = Observable.interval(300, MILLISECONDS);
        System.out.println("Trying combine with interval sources ");
        //Observable.zip(source8,source9,(s8,s9)->"Source 1-> "+s8+" Source2-> "+s9).subscribe(System.out::println);//0 1 2 since sleep is only for 900 millisecs
        //Thread.sleep(2000);
        /*
        Source 1-> 0 Source2-> 0
        Source 1-> 1 Source2-> 1
        Since both sources would never match so it caches the value of faster source and tries to pair it
        with the slow source .Bcoz of this you might have a stackoverflow problem as the caching values
        can get large and throw memory errors
         */
        Observable.combineLatest(source8, source9, (s8, s9) -> "Source 1-> " + s8 + " Source2-> " + s9).subscribe(System.out::println);//0 1 2 since sleep is only for 900 millisecs
        Thread.sleep(2000);
        /*Compared to zip it takes pairs latest available value and combines it with other values of source
        Source 1-> 0 Source2-> 2
        Source 1-> 0 Source2-> 3
        Source 1-> 0 Source2-> 4
        Source 1-> 0 Source2-> 5
        Source 1-> 1 Source2-> 5
        Source 1-> 1 Source2-> 6
        Source 1-> 1 Source2-> 7
        Source 1-> 1 Source2-> 8
        */

        Observable<Long> source10 = Observable.interval(1, SECONDS);
        Observable<Long> source11 = Observable.interval(300, MILLISECONDS);
        System.out.println("Trying withLatestFrom with interval sources ");
        source10.withLatestFrom(source11, (s10, s11) -> "Source 10-> " + s10 + " Source 11-> " + s11).subscribe(System.out::println);//0 1 2 since sleep is only for 900 millisecs
        Thread.sleep(5000);
        /*
        withLatestFrom is similiar to combine except
        you wouldnt have values of pair repeated ,if one value of source1 is paired with other value of source2,
        then the values of  source2 wouldnt repeat with other values of source1.
        Source 10-> 0 Source 11-> 2
        Source 10-> 1 Source 11-> 5
        Source 10-> 2 Source 11-> 8
        Source 10-> 3 Source 11-> 12
        Source 10-> 4 Source 11-> 15
         */


        Observable<Long> source4 = Observable.interval(1, SECONDS);
        Observable<Long> source5 = Observable.interval(300, MILLISECONDS);
        Observable.ambArray(source4, source5).subscribe(System.out::println);//0 1 2 since sleep is only for 900 millisecs
        //ambigious ambArray function picks up value from source5 as it is faster than source4
        Thread.sleep(900);

        System.out.println("Trying group by ");
        Observable<String> colors = Observable.just("blue", "green", "black", "brown", "gray", "red", "violet");
        Observable<GroupedObservable<Character, String>> groupedObservable = colors.groupBy((s) -> s.charAt(0));
        groupedObservable.flatMapSingle((e) -> e.toList()).subscribe(System.out::println);
        /*Output
            [blue, black, brown]
            [red]
            [violet]
            [green, gray]
         */
        groupedObservable.flatMapSingle(
                e -> e.reduce("", (x, y) -> x.equals("") ? y : x + "," + y)
                        //gives values as , separated --> blue,black,brown
                        .map(g -> e.getKey() + ":" + g))
                        //gives value as b: blue,black,brown
                .subscribe(System.out::println);
        /*
        b:,blue,black,brown
        r:,red
        v:,violet
        g:,green,gray
         */
    }
}
