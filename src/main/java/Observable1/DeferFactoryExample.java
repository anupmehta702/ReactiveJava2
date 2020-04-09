package Observable1;

import io.reactivex.Observable;

public class DeferFactoryExample {
    public static int start = 0, end = 2;

    public static void main(String[] args) {
        int a = 0, b = 2;
        Observable<Integer> source = Observable.range(a, b);
        source.subscribe(v -> System.out.println(" Sub #1 val ->" + v));
        b = 4;//despite changing value of b ,below sub prints only 0,1
        source.subscribe(v -> System.out.println(" Sub #2 val ->" + v));

        System.out.println(" -- Testing with defer --");
        Observable<Integer> deferSource = Observable.defer(() -> Observable.range(start, end));
        deferSource.subscribe(v -> System.out.println(" deferSub #1 val ->" + v));
        end = 4;
        deferSource.subscribe(v -> System.out.println(" deferSub #2 val ->" + v));
        /*using defer ,you defer the creation of Observable ,in the above two cases two Observables were
        created ,one with 0,2 and other with 0,4
        This can be dangerous if your subscribers are huge in number ,it would create those many Observables
        */
    }
}
/*Output
 Sub #1 val ->0
 Sub #1 val ->1
 Sub #2 val ->0
 Sub #2 val ->1
 -- Testing with defer --
 deferSub #1 val ->0
 deferSub #1 val ->1
 deferSub #2 val ->0
 deferSub #2 val ->1
 deferSub #2 val ->2
 deferSub #2 val ->3
 */
