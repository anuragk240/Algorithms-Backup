import java.util.Scanner;

public class PlacingParentheses {
    private static long getMaximValue(String exp) {
		//write your code here
		StringBuilder op = new StringBuilder();
		for(int i = 1; i < exp.length(); i += 2){
			op.append(exp.charAt(i));
		}
		int num = op.length() + 1;
		long[][] max = new long[num][num];
		long[][] min = new long[num][num];
		
		for(int i = 0; i < num; ++i){
			min[i][i] = max[i][i] = Integer.parseInt(exp.substring(2 * i, 2 * i + 1));
		}
		
		for(int d = 1; d < num; ++d){
			for(int i = 0; i + d < num; ++i){
				optimum(i, i + d, max, min, op.toString());
			}
		}
		
		/*for(int i = 0; i < num; ++i){
			for(int j = 0; j < num; ++j){
				System.out.print(max[i][j] + " ");
			}
			System.out.println();
		}
		
		System.out.println();
		
		for(int i = 0; i < num; ++i){
			for(int j = 0; j < num; ++j){
				System.out.print(min[i][j] + " ");
			}
			System.out.println();
		}
		*/
		return max[0][num - 1];
    }
	
	private static void optimum(int i, int j, long[][] max, long[][] min, String operator){
		long m1, m2, m3, m4, a, b;
		m1 = eval(max[i][i],  max[i + 1][j], operator.charAt(i));
		m2 = eval(max[i][i],  min[i + 1][j], operator.charAt(i));
		m3 = eval(min[i][i],  max[i + 1][j], operator.charAt(i));
		m4 = eval(min[i][i],  min[i + 1][j], operator.charAt(i));
		a = max(m1, m2, m3, m4);
		b = min(m1, m2, m3, m4);
		for(int op = i + 1; op < j; ++op){
			m1 = eval(max[i][op],  max[op + 1][j], operator.charAt(op));
			m2 = eval(max[i][op],  min[op + 1][j], operator.charAt(op));
			m3 = eval(min[i][op],  max[op + 1][j], operator.charAt(op));
			m4 = eval(min[i][op],  min[op + 1][j], operator.charAt(op));
			if(max(m1, m2, m3, m4) > a) a = max(m1, m2, m3, m4);
			if(min(m1, m2, m3, m4) < b) b = min(m1, m2, m3, m4);
		}
		max[i][j] = a;
		min[i][j] = b;
	}
	
	public static long min(long a, long b, long c, long d) {
		if (a <= b && a <= c && a <= d) return a;
		if (b <= a && b <= c && b <= d) return b;
		if (c <= a && c <= b && c <= d) return c;
		return d;
	}
	
	public static long max(long a, long b, long c, long d) {
		if (a >= b && a >= c && a >= d) return a;
		if (b >= a && b >= c && b >= d) return b;
		if (c >= a && c >= b && c >= d) return c;
		return d;
	}

    private static long eval(long a, long b, char op) {
        if (op == '+') {
            return a + b;
        } else if (op == '-') {
            return a - b;
        } else if (op == '*') {
            return a * b;
        } else {
            assert false;
            return 0;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String exp = scanner.next();
        System.out.println(getMaximValue(exp));
    }
}

