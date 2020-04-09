package Observable1;


import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class DifferentObservables {
    public static void main(String[] args) {
        System.out.println("-- Single Observable --");
        Single<String> singleSource = Observable.just("a","b").first("default val");
        //first() predicate return single Observable which has success method instead of onNext and complete
        singleSource.subscribe(System.out::println,Throwable::printStackTrace); //output -->  a
        //above subscriber does not have complete method

        System.out.println("-- mayBe Observable --");
        Observable<String> mayBeSource = Observable.just("a","b","bcd").filter((v)->{
            if(v.length()>2)
                return true;
            return false;
        });
        //filter() predicate maybe would return a value or not
        mayBeSource.subscribe(System.out::println,Throwable::printStackTrace,()-> System.out.println("Complete !")); //output -->  a

        System.out.println("-- completable Observable -- ");
        Completable completableSource = Completable.fromRunnable(()-> System.out.println("Running a task"));
        completableSource.subscribe(()-> System.out.println("Completable completed "));
        //above method has only complete method
    }

}
/*Output
-- Single Observable --
a
-- mayBe Observable --
bcd
Complete !
-- completable Observable --
Running a task
Completable completed
 */
