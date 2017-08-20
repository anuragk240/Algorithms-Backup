import java.io.*;
import java.util.*;

	class Position {
    Position(int column, int row) {
        this.column = column;
        this.row = row;
    }

    int column;
    int row;
}

public class Diet {

    BufferedReader br;
    PrintWriter out;
    StringTokenizer st;
    boolean eof;
	public static final double SMALL_EPSILON = 0.000000000000000001;
	public static final double LONG_EPSILON = 0.000001;

    int solveDietProblem(int n, int m, double A[][], double[] b, double[] c, double[] x) {
		Arrays.fill(x, 1);
		// Write your code here
		int[] basic = new int[n + m + 1];
		int[] non_basic = new int[n + m + 1];
	
		double v = 0;
		
		Arrays.fill(basic, -1);
		Arrays.fill(non_basic, -1);
		
		for(int i = 1; i <= m; ++i){
			non_basic[i] = i;
		}
		
		for(int i = m + 1; i <= n + m; ++i){
			basic[i] = i - m - 1;
		}
		
		v = initialize(non_basic, basic, A, b, c);
		
		if(v == Double.POSITIVE_INFINITY) return -1;
		
		v = iterate(non_basic, basic, A, b, c, v);
		
		if(v == Double.POSITIVE_INFINITY) return 1;
		
		for(int i = 0; i < m; ++i){
			if(basic[i] != -1){
				x[i] = b[basic[i]];
			}else {
				x[i] = 0;
			}
		}
	  
		return 0;
    }
	
	private double iterate(int[] non_basic, int[] basic, double[][] A, double[] b, double[] c, double v){
		int i = 0;
		while(true){
			if(non_basic[i] != -1 && c[non_basic[i]] > 0 + LONG_EPSILON){
				int entering = i;
				int leaving = -1;
				int column = non_basic[i];
				
				double min = Double.POSITIVE_INFINITY;
				for(int j = 0; j < basic.length; ++j){
					int row = basic[j];
					if(basic[j] != -1 && A[row][column] > 0 + LONG_EPSILON){
						double ratio = b[row] / A[row][column];
						min = (min < ratio) ? min : ratio;
						leaving = (min < ratio) ? leaving : j;
					}
				}
				
				if(min == Double.POSITIVE_INFINITY)
					return min;
				else{
					// System.out.println("\n\nBefore pivot inside iterate: ");
					// printbasic_non_basic(non_basic, basic);
					// printSlackForm(A, b, c, v);
					v = pivot(non_basic, basic, A, b, c, v, leaving, entering);
					// System.out.println("\n\nAfter pivot inside iterate: ");
					// printbasic_non_basic(non_basic, basic);
					// printSlackForm(A, b, c, v);
				}
				i = 0;
			}else {
				++i;
				if(i >= non_basic.length) break;
			}
		}
		
		return v;
	}
	
	private double initialize(int[] non_basic, int[] basic, double[][] A, double[] b, double[] c){
		double min = b[0];
		int k = 0;
		for(int i = 1; i < b.length; ++i){
			min = (min < b[i]) ? min : b[i];
			k = (min < b[i]) ? k : i;
		}
		
		if(min >= 0){
			for(int i = 1; i < non_basic.length && non_basic[i] != -1; ++i)
				non_basic[i]--;
			
			for(int i = 0; i < non_basic.length - 1; ++i){
				non_basic[i] = non_basic[i + 1];
				basic[i] = basic[i + 1];
			}
		
			basic[basic.length - 1] = non_basic[non_basic.length - 1] = -1;
			return 0;
		}
		
		int leaving = c.length + k + 1;
		
		double[] c1 = new double[c.length + 1];
		Arrays.fill(c1, 0);
		c1[0] = -1;
		
		double[][] A1 = new double[A.length][A[0].length + 1];
		
		for(int i = 0; i < A1.length; ++i){
			A1[i][0] = -1;
			for(int j = 1; j < A1[0].length; ++j){
				A1[i][j] = A[i][j - 1];
			}
		}
		
		non_basic[0] = 0;
		// System.out.println("\nBefore pivot initialize: ");
		// printbasic_non_basic(non_basic, basic);
		// printSlackForm(A1, b, c1, 0);
		
		double v = pivot(non_basic, basic, A1, b, c1, 0, leaving, 0);
		
		// System.out.println("\nAfter pivot initialize: ");
		// printbasic_non_basic(non_basic, basic);
		// printSlackForm(A1, b, c1, v);
		
		v = iterate(non_basic, basic, A1, b, c1, v);
		
		// System.out.println("\nAfter pivot iterate: ");
		// printbasic_non_basic(non_basic, basic);
		// printSlackForm(A1, b, c1, v);
		
		if(v == Double.POSITIVE_INFINITY) return v;
		//Equality with zero!
		if((basic[0] != -1 && Math.abs(b[basic[0]]) < LONG_EPSILON) || non_basic[0] != -1){
			if(basic[0] != -1){
				//degenerate pivot
				for(int i = 0; i < non_basic.length; ++i){
					if(non_basic[i] != -1 && A1[basic[0]][non_basic[i]] != 0){
						v = pivot(non_basic, basic, A1, b, c1, v, 0, i);
						break;
					}
				}
			}
			
			for(int i = 0; i < A.length; ++i){
				for(int  j = 0; j < non_basic[0]; ++j){
					A[i][j] = A1[i][j];
				}
				for(int j = non_basic[0]; j < A1[0].length - 1; ++j){
					A[i][j] = A1[i][j + 1];
				}
			}
			
			
			for(int i = 1; i < non_basic.length; ++i){
				if(non_basic[i] != -1 && non_basic[i] > non_basic[0]){
					non_basic[i]--;
				}
			}
			
			for(int i = 0; i < non_basic.length - 1; ++i){
				non_basic[i] = non_basic[i + 1];
				basic[i] = basic[i + 1];
			}
			
			basic[basic.length - 1] = non_basic[non_basic.length - 1] = -1;
			
			double[] temp = new double[A.length + A[0].length];
			Arrays.fill(temp, 0);
			for(int i = 0; i < c.length; ++i){
				temp[i] = c[i];
			}
			
			for(int i = 0; i < c.length; ++i){
				int row;
				if(basic[i] != -1){
					row = basic[i];
				}else continue;
				
				int column = i;
				for(int j = 0; j < non_basic.length; ++j){
					if(non_basic[j] != -1){
						temp[j] = temp[j] - temp[column] * A[row][non_basic[j]];
					}
				}
				v = v + b[row] * temp[column];
				temp[column] = 0;
			}
			
			for(int i = 0; i < temp.length; ++i){
				int variable_num = i;
				if(non_basic[variable_num] != -1){
					int index = non_basic[variable_num];
					c[index] = temp[i];
				}
			}
			
			return v;
		}else {
			return Double.POSITIVE_INFINITY;
		}
	}
	
	private double pivot(int[] non_basic, int[] basic, double[][] A, double[] b, double[] c, double v, int bi, int ni){
		int row = basic[bi];
		int column = non_basic[ni];
		
		//update b
		//System.out.println("row:column = " + row + ":" + column);
		b[row] = b[row] / A[row][column];
		//update pivot row
		for(int i = 0; i < A[0].length; ++i){
			if(i != column){
				A[row][i] /= A[row][column];
			}
		}
		A[row][column] = 1.0 / A[row][column];
		
		//update other b[i]
		for(int i = 0; i < b.length; ++i){
			if(i != row){
				b[i] -= A[i][column] * b[row];
			}
		}
		
		for(int i = 0; i < A.length; ++i){
			if(i == row) continue;
			for(int j = 0; j < A[0].length; ++j){
				if(j != column){
					A[i][j] = A[i][j] - A[i][column] * A[row][j]; 
				}
			}
			A[i][column] = (-1.0) * A[i][column] * A[row][column];
		}
		
		for(int i = 0; i < c.length; ++i){
			if(i != column){
				c[i] = c[i] - c[column] * A[row][i];
			}
		}
		c[column] = (-1.0) * c[column] * A[row][column];
		
		v = v + b[row] * c[column];
		
		//swap basic and non_basic variable
		basic[bi] = -1;
		non_basic[ni] = -1;
		basic[ni] = row;
		non_basic[bi] = column;
		
		return v;
	}
	
	private void printbasic_non_basic(int[] non_basic, int[] basic){
		System.out.print("basic:");
		for(int i = 0; i < basic.length; ++i){
			System.out.print(basic[i] + " ");
		}
		System.out.print("\nnon_basic:");
		
		for(int i = 0; i < non_basic.length; ++i){
			System.out.print(non_basic[i] + " ");
		}
		System.out.println();
	}
	
	private void printSlackForm(double[][] A, double[] b, double[] c, double v){
		System.out.println("v c[]:");
		System.out.print(v + "\t");
		for(int i = 0; i < c.length; ++i){
			System.out.print(c[i] + " ");
		}
		System.out.println();
		
		System.out.println("b A:");
		for(int i = 0; i < A.length; ++i){
			System.out.print(b[i] + "\t");
			for(int j = 0; j < A[0].length; ++j){
				System.out.print(A[i][j] + " ");
			}
			System.out.println();
		}
	}

	
    void solve() throws IOException {
        int n = nextInt();
        int m = nextInt();
        double[][] A = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                A[i][j] = nextInt();
            }
        }
        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
            b[i] = nextInt();
        }
        double[] c = new double[m];
        for (int i = 0; i < m; i++) {
            c[i] = nextInt();
        }
        double[] ansx = new double[m];
        int anst = solveDietProblem(n, m, A, b, c, ansx);
        if (anst == -1) {
            out.printf("No solution\n");
            return;
        }
        if (anst == 0) {
            out.printf("Bounded solution\n");
            for (int i = 0; i < m; i++) {
                out.printf("%.18f%c", ansx[i], i + 1 == m ? '\n' : ' ');
            }
            return;
        }
        if (anst == 1) {
            out.printf("Infinity\n");
            return;
        }
    }
    Diet() throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out);
        solve();
        out.close();
    }

    public static void main(String[] args) throws IOException {
        new Diet();
    }

    String nextToken() {
        while (st == null || !st.hasMoreTokens()) {
            try {
                st = new StringTokenizer(br.readLine());
            } catch (Exception e) {
                eof = true;
                return null;
            }
        }
        return st.nextToken();
    }

    int nextInt() throws IOException {
        return Integer.parseInt(nextToken());
    }
}
