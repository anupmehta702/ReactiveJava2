package AlternateToFlowables7;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class TwoWindowTest {
    public static void main(String[] args) {
        Disposable sub = Observable.range(1, 50)
                .window(10) //returns  Observable<Observable<Integer>> whereas buffer returns Observable<<List<Integer>>
                .flatMap(e -> {
                    return e.take(10);
                })
                //.flatMapSingle(e->e.count())//prints val --> 10 5 times
                .subscribe((v) -> {
                    System.out.println("val -->" + v);
                });
        sub.dispose();
    }
}
/*Output


 */
