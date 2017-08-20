import java.util.*;

public class DotProduct {
    private static long maxDotProduct(int[] a, int[] b) {
        //write your code here
        long result = 0;
        a = sort(a);
        b = sort(b);
        for(int i = a.length - 1; i >= 0; --i){
            result += a[i] * (long)b[i];
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            b[i] = scanner.nextInt();
        }
        System.out.println(maxDotProduct(a, b));
    }

    public static int[] sort(int[] a) {
        for(int i = 0; i < a.length; ++i){
        	for(int j = 0; j < i; ++j){
        	    if(a[i] < a[j]){
        	        swap(i, j, a);
        	    }
        	}
        }
        return a;
    }
    
    public static void swap(int a, int b, int[] array){
    	int temp = array[a];
    	array[a] = array[b];
    	array[b] = temp;
    }
}

