import java.util.*;

public class DifferentSummands {
    private static List<Integer> optimalSummands(int n) {
        List<Integer> summands = new ArrayList<Integer>();
        //write your code here
        int size = (int) Math.sqrt(2 * n);
        int sum = size * (size + 1) / 2;
        int last_num;
        if(n > sum){
            last_num = size + n - sum;
        } else if(n == sum){
            last_num = size;
        } else {
            --size;
            last_num = size + n - (size * (size + 1) / 2);
        }

        for(int i = 1; i < size; ++i){
            summands.add(i);
        }
        summands.add(last_num);
        return summands;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Integer> summands = optimalSummands(n);
        System.out.println(summands.size());
        for (Integer summand : summands) {
            System.out.print(summand + " ");
        }
    }
}

