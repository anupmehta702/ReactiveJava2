package Operators2;

import io.reactivex.Observable;

import java.util.Comparator;
import java.util.HashSet;

import static java.util.concurrent.TimeUnit.SECONDS;

public class OperatorTest {

    public static void main(String[] args) throws InterruptedException {
        Observable<String> source = Observable.just("alpha","beta","gamma","omega");

        System.out.println("Filter with length > 4 ,val -> ");
        source.filter(e->e.length()>4)
                .subscribe((v)-> System.out.print(" "+v));

        System.out.println();
        System.out.println(" take 2 ,val -> ");
        source.take(2)
                .subscribe((v)-> System.out.print(" "+v));

        System.out.println();
        System.out.println(" skip 2 ,val -> ");
        source.skip(2)
                .subscribe((v)-> System.out.print(" "+v));

        System.out.println();
        System.out.println(" takeWhile  length > 4 ,val -> ");
        source.takeWhile(e->e.length()>4)
                .subscribe((v)-> System.out.print(" "+v));

        System.out.println();
        System.out.println(" skipWhile  length > 4 ,val -> ");
        source.skipWhile(e->e.length()>4)
                .subscribe((v)-> System.out.print(" "+v));

        System.out.println();
        System.out.println(" distinct for length ,val -> ");
        source.distinct(e->e.length())
                .subscribe((v)-> System.out.print(" "+v));

        System.out.println();
        System.out.println(" element at 2 ,val -> ");
        source.elementAt(2)
                .subscribe((v)-> System.out.print(" "+v));

        System.out.println();
        System.out.println(" map with length ,val -> ");
        source.map(e->e.length())
                .subscribe((v)-> System.out.print(" "+v));

        System.out.println();
        System.out.println("startWith val -> ");
        source.startWith("Here comes values ->")
                .subscribe((v)-> System.out.print(" "+v));

        System.out.println();
        System.out.println("cast val -> ");
        source.cast(Object.class)
                .subscribe((v)-> System.out.print(" "+v));

        System.out.println();
        System.out.println("DefaultIfEmpty val -> ");
        Observable.empty().defaultIfEmpty("Default value").subscribe((v)->System.out.print(" "+v));

        System.out.println();
        System.out.println("SwitchIfEmpty to another source val -> ");
        Observable.empty().switchIfEmpty(source).subscribe((v)->System.out.print(" "+v));

        System.out.println();
        System.out.println("delay val -> ");
        source.delay(1, SECONDS).subscribe((v)-> System.out.print(" "+v));
        Thread.sleep(4000);

        System.out.println();
        System.out.println("sorted val -> ");
        Observable.just(5,4,1,2,3).sorted(Comparator.reverseOrder()).subscribe((v)-> System.out.print(" "+v));//5 4 3 2 1

        System.out.println();
        System.out.println("repeat val -> ");
        Observable.just(5,4,1,2,3).repeat(2).subscribe((v)-> System.out.print(" "+v));//5 4 1 2 3 5 4 1 2 3

        System.out.println();
        System.out.println("scan val -> ");
        Observable.just(5,4,1,2,3).scan((total,next)->total+next).subscribe((v)-> System.out.print(" "+v));//5 9 10 12 15

        System.out.println();
        System.out.println("all val -> ");
        source.all(e->e.length() >= 4).subscribe((v)-> System.out.print(" "+v));//true

        System.out.println();
        System.out.println("any val -> ");
        source.any(e->e.length() > 4).subscribe((v)-> System.out.print(" "+v));//true

        System.out.println();
        System.out.println("reduce val -> ");
        source.reduce((a,b)->a+(b.equals("")?"":","+b)).subscribe((v)-> System.out.print(" "+v));
        //reduce - it reduces the output to a single value ,(a,b) a is first val ,b is next value
        //alpha,beta,gamma,omega

        System.out.println();
        System.out.println("toSortedList val -> ");
        Observable.just(5,4,1,2,3).toSortedList().subscribe((v)-> System.out.print(" "+v));//[1, 2, 3, 4, 5]

        System.out.println();
        System.out.println("toMap val -> ");
        source.toMap((v)->v.length()).subscribe((v)-> System.out.print(" "+v));

        System.out.println();
        System.out.println("toMultiMap val -> ");
        source.toMultimap((v)->v.length())
                .subscribe((v)-> System.out.print(" "+v));//{4=[beta], 5=[alpha, gamma, omega]}


        System.out.println();
        System.out.println("toMultiMap(new) val -> ");
        source.toMultimap(v->v.charAt(0),v->v.length())
                .subscribe(System.out::println);//{a=[5], b=[4], g=[5], o=[5]}

        System.out.println();
        System.out.println("collect val -> ");
        source.collect(HashSet::new,(v,e)->v.add(e))
                .subscribe((v)-> System.out.print(" "+v));//[alpha, beta, gamma, omega]

        System.out.println();
        System.out.println("Error(return) operator val -> ");
        Observable.just(1,2,3,0,4).map(i->5/i).onErrorReturnItem(-1)
                .subscribe((v)-> System.out.print(" "+v));// 5 2 1 -1


        System.out.println();
        System.out.println("Error(return) operator val -> ");
        Observable.just(1,2,3,0,4).map(i->5/i).onErrorResumeNext(Observable.just(44,55,66))
                .subscribe((v)-> System.out.print(" "+v));// 5 2 1 44 55 66

        System.out.println();
        System.out.println("Action operator operator val -> ");
        Observable.just(1,2,3,0,4).doOnNext((e)-> System.out.println("Next element is coming"))
                .doOnComplete(()-> System.out.println("complete !"))
                .doOnError((e)-> System.out.println("Error !"))
                .map(i->5/i)//.onErrorResumeNext(Observable.just(44,55,66))
                .subscribe((v)-> System.out.print(" "+v));
        /*
        Next element is coming
        5Next element is coming
        2Next element is coming
        1Next element is coming
        ArithmeticException: / zero
         */


    }
}
/*Output
Filter with length > 4 ,val ->
 alpha gamma omega
 take 2 ,val ->
 alpha beta
 skip 2 ,val ->
 gamma omega
 takeWhile  length > 4 ,val ->
 alpha
 skipWhile  length > 4 ,val ->
 beta gamma omega
 distinct for length ,val ->
 alpha beta
 element at 2 ,val ->
 gamma
 map with length ,val ->
 5 4 5 5
startWith val ->
 Here comes values -> alpha beta gamma omega
cast val ->
 alpha beta gamma omega
DefaultIfEmpty val ->
 Default value
SwitchIfEmpty to another source val ->
 alpha beta gamma omega
delay val ->
 alpha beta gamma omega
sorted val ->
 5 4 3 2 1
repeat val ->
 5 4 1 2 3 5 4 1 2 3
scan val ->
 5 9 10 12 15
all val ->
 true
any val ->
 true
reduce val ->
 alpha,beta,gamma,omega
toSortedList val ->
 [1, 2, 3, 4, 5]

 */