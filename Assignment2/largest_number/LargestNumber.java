import java.util.*;

public class LargestNumber {
    private static String largestNumber(String[] a) {
        //write your code here
        String result = "";
        for(int i = 0; i < a.length; ++i){
            result = result.concat(optimum(a, a.length - i));
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        String[] a = new String[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.next();
        }
        System.out.println(largestNumber(a));
    }

    private static String optimum(String[] a, int num){
        String str = a[0], sum1, sum2;
        int index = 0;
        for(int i = 1; i < num; ++i){
            sum1 = str + a[i];
            sum2 = a[i] + str;
            if(sum1.compareTo(sum2) < 0){
                str = a[i];
                index = i;
            }
        }
        a[index] = a[num - 1];
        a[num - 1] = null;
        return str;
    }
}

