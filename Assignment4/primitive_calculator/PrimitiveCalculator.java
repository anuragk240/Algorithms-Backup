import java.util.*;

public class PrimitiveCalculator {
    private static List<Integer> optimal_sequence(int n) {
        List<Integer> sequence = new ArrayList<Integer>();
		int[] arr = new int[n + 1];
		arr[1] = 0;
		int d1, d2, d3;
		for(int i = 2; i <= n; ++i){
			d1 = d2 = d3 = Integer.MAX_VALUE;
			if (i % 3 == 0) {
                d3 = arr[i / 3];
            }
			if (i % 2 == 0) {
                d2 = arr[i / 2];
            }
            
			d1 = arr[i - 1];
			arr[i] = min(d1, d2, d3) + 1;
		}
		/*for(int  i = 1; i < arr.length; ++i){
			System.out.print(arr[i] + " ");
		}
		System.out.println();*/
		int temp = n;
        for(int i = n; i >= 1;){
			d1 = d2 = d3 = Integer.MAX_VALUE;
			sequence.add(i);
			if (i % 3 == 0 && (arr[i] - arr[i/3] == 1)){
				i /= 3;
			} else if (i % 2 == 0 && (arr[i] - arr[i/2] == 1)){
				i /= 2;
			} else{
				i--;
			}
		}
        Collections.reverse(sequence);
        return sequence;
    }
	
	public static int min(int a, int b, int c) {
     if (a <= b && a <= c) return a;
     if (b <= a && b <= c) return b;
     return c;
}

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Integer> sequence = optimal_sequence(n);
        System.out.println(sequence.size() - 1);
        for (Integer x : sequence) {
            System.out.print(x + " ");
        }
    }
}

