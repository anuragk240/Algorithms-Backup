import java.util.*;

class EditDistance {
  public static int EditDistance(String s, String t) {
    //write your code here
	int[][] mat = new int[s.length() + 1][t.length() + 1];
	for(int i = 0; i <= s.length(); ++i){
		mat[i][0] = i;
	}
	for(int i = 0; i <= t.length(); ++i){
		mat[0][i] = i;
	}
	int t1, t2, t3;
	for(int i = 1; i <= s.length(); ++i){
		for(int j = 1; j <= t.length(); ++j){
			if(s.charAt(i - 1) == t.charAt(j - 1)){
				t1 = mat[i - 1][j - 1];
			} else {
				t1 = mat[i - 1][j - 1] + 1;
			}
			t2 = mat[i - 1][j] + 1;
			t3 = mat[i][j - 1] + 1;
			mat[i][j] = min(t1, t2, t3);
			//System.out.print(mat[i][j] + " ");
		}
		//System.out.println();
	}
    return mat[s.length()][t.length()];
  }
  
  	public static int min(int a, int b, int c) {
		if (a <= b && a <= c) return a;
		if (b <= a && b <= c) return b;
		return c;
	}
  public static void main(String args[]) {
    Scanner scan = new Scanner(System.in);

    String s = scan.next();
    String t = scan.next();

    System.out.println(EditDistance(s, t));
  }

}
