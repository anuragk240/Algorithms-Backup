import java.util.Scanner;

public class FractionalKnapsack {
	
	private static double[] ratio = null;
	private static int[] mvalues = null;
	private static int[] mweights = null;
	private static int n_items;
	
    private static double getOptimalValue(int capacity, int[] values, int[] weights) {
        double value = 0;
        //write your code here
        n_items = values.length;
        mvalues = values;
        mweights = weights;
        ratio = new double[n_items + 1];

        for(int i = 0; i < n_items; ++i){
            ratio[i] = (double)mvalues[i] / mweights[i];
        }
        
        /*unsorted array
        for(int i = 0; i < n_items; ++i){
        	System.out.println(data[i].toString());
        }*/

        sort();
        
        /*/print sorted array
        for(int i = 0; i < n_items; ++i){
        	System.out.println(data[i].toString());
        }*/

        int i = n_items - 1;
        double remaining = capacity;
        while(remaining > 0 && i >= 0) {
            if(mweights[i] <= remaining) {
                value += mvalues[i];
                remaining -= mweights[i];
            } else {
                value += remaining/mweights[i] * mvalues[i];
                remaining = 0;
            }
            --i;
            //System.out.println(String.valueOf(value) + " i=" + i);
        }

        return value;
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int capacity = scanner.nextInt();
        int[] values = new int[n];
        int[] weights = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = scanner.nextInt();
            weights[i] = scanner.nextInt();
        }
        System.out.println(getOptimalValue(capacity, values, weights));
        scanner.close();
    }
    
    public static void sort() {
        for(int i = 0; i < n_items; ++i){
        	for(int j = 0; j < i; ++j){
        		if(ratio[i] < ratio[j]){
        			swap(i, j, ratio);
        			swap(i, j, mvalues);
        			swap(i, j, mweights);
        		}
        	}
        }
    }
    
    public static void swap(int a, int b, double[] array){
    	double temp = array[a];
    	array[a] = array[b];
    	array[b] = temp;
    }
    
    public static void swap(int a, int b, int[] array){
    	int temp = array[a];
    	array[a] = array[b];
    	array[b] = temp;
    }
}

