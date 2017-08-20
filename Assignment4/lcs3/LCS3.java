import java.util.*;

public class LCS3 {

    private static int lcs3(int[] a, int[] b, int[] c) {
        //Write your code here
		int[][][] data = new int[a.length + 1][b.length + 1][c.length + 1];
		for(int i = 0; i <= a.length; ++i){
			for(int j = 0; j <= b.length; ++j){
				data[i][j][0] = 0;
			}
		}
		
		for(int i = 0; i <= b.length; ++i){
			for(int j = 0; j <= c.length; ++j){
				data[0][i][j] = 0;
			}
		}
		
		for(int i = 0; i <= a.length; ++i){
			for(int j = 0; j <= c.length; ++j){
				data[i][0][j] = 0;
			}
		}
		int[] t = new int[7];
			
		for(int i = 1; i <= a.length; ++i){
			for(int j = 1; j <= b.length; ++j){
				for(int k = 1; k <= c.length; ++k){
					if(a[i - 1] == b[j - 1] && b[j - 1] == c[k - 1]){
						t[0] = data[i-1][j-1][k-1] + 1;
					} else {
						t[0] = data[i-1][j-1][k-1];
					}
					t[1] = data[i][j][k-1];
					t[2] = data[i][j-1][k];
					t[3] = data[i-1][j][k];
					t[4] = data[i-1][j-1][k];
					t[5] = data[i-1][j][k-1];
					t[6] = data[i][j-1][k-1];
					Arrays.sort(t);
					data[i][j][k] = t[6];
				}
			}
		}
		/*
		for(int i = 0; i <= a.length; ++i){
			for(int j = 0; j <= b.length; ++j){
				for(int k = 0; k <= c.length; ++k){
					System.out.print(data[i][j][k] + " ");
				}
				System.out.println();
			}
			System.out.println();
		}*/
		
        return data[a.length][b.length][c.length];
    }

    public static void main(String[] args) {
		//stressTest();
		
        Scanner scanner = new Scanner(System.in);
        int an = scanner.nextInt();
        int[] a = new int[an];
        for (int i = 0; i < an; i++) {
            a[i] = scanner.nextInt();
        }
        int bn = scanner.nextInt();
        int[] b = new int[bn];
        for (int i = 0; i < bn; i++) {
            b[i] = scanner.nextInt();
        }
        int cn = scanner.nextInt();
        int[] c = new int[cn];
        for (int i = 0; i < cn; i++) {
            c[i] = scanner.nextInt();
        }
        System.out.println(lcs3(a, b, c));
		
    }
	
	private static void stressTest(){
		int n = 100;
		int[] a, b, c;
		Random r = new Random();
		a = new int[n];
		b = new int[n];
		c = new int[n];
		for (int i = 0; i < n; i++) {
            a[i] = r.nextInt(n+1);
			b[i] = r.nextInt(n+1);
			c[i] = r.nextInt(n+1);
        }
		
		System.out.println("Input generated");
		long startTime = System.nanoTime();
		System.out.println(lcs3(a, b, c));
		long endTime = System.nanoTime();

		long duration = (endTime - startTime);
		System.out.format("Time = %.3f", (double)duration / 1000000000);
	}
}

