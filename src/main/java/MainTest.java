import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainTest {
    public static void main(String args[]) throws Exception {
        System.out.println("Hello World");
        Integer[] arr = new Integer[]{1, 4, 3, 5, 5,6};
        findSum(arr, 9);

    }

    public static void findSum(Integer[] arr, Integer sum) {
        Map<Integer, List<Integer>> inputSummary = new HashMap();
        for (int i = 0; i < arr.length; i++) {
            if (inputSummary.containsKey(arr[i])) {
                List<Integer> indexes = inputSummary.get(arr[i]);
                indexes.add(i);
            } else {
                List<Integer> index = new ArrayList();
                index.add(i);
                inputSummary.put(arr[i], index);
            }
        }
        for (int i = 0; i < arr.length; i++) {
            int diff = Math.abs(sum - arr[i]);
            if (inputSummary.containsKey(diff)) {
                int secondVarIndex = inputSummary.get(diff).get(0);
                System.out.println("Var1 -->" + arr[i] + " at index-->" + i + " , Var2 -->" + diff + " at index-->" + secondVarIndex);
                //break;
            }
        }
    }
}
//[1,4,3,5,5], sum=9  -- > map(int,index)
//[8,5,6,4,4]
// any one combination

