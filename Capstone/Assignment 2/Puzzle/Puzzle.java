import java.util.*;

public class Puzzle{
	
	static Piece[] pieces = null;
	static Piece[][] matrix = null;
	static boolean solved = false;
	
	public static void main(String[] args){
		int n = 5;
		int numpieces = n * n;
		Scanner scanner = new Scanner(System.in);
		pieces = new Piece[numpieces];
		matrix = new Piece[n + 2][n + 2];
		for(int i = 0; i < numpieces; ++i){
			pieces[i] = Piece.getPiece(scanner.nextLine());
		}

		for(int i = 0; i <= n + 1; ++i){
			matrix[0][i] = new Piece("black");
			matrix[i][0] = new Piece("black");
			matrix[n + 1][i] = new Piece("black");
			matrix[i][n + 1] = new Piece("black");
		}
		
		solved = false;
		iterate(n, 0);
		
		int k = n - 1;
		for(int i = 0; i < n; ++i){
			for(int j = 0; j < n; ++j){
				System.out.print(matrix[i + 1][j + 1].toString());
				if(j < n - 1){
					System.out.print(';');
				}
				--k;
			}
			System.out.println();
		}
	}
	
	public static void iterate(int n, int matrixpos){
		if(matrixpos >= n * n){
			solved = true;
			return;
		}
		int total = n * n;
		int row = (int)(matrixpos / n) + 1;
		int column = (matrixpos) % n + 1;
		
		int pieceposition = total - matrixpos - 1;
		
		for(int i = pieceposition; i >= 0; --i){
			Piece temp = pieces[i];
			//check if it can fit in given position
			if(isfit(temp, row, column)){
				//System.out.println("row,col: " + row + ":" + column);
				//swap
				int usedpieces = matrixpos + 1;
				Piece t = pieces[total - usedpieces];
				pieces[total - usedpieces] = pieces[i];
				pieces[i] = t;
				
				matrix[row][column] = temp;
				
				iterate(n, matrixpos + 1);
				if(solved){
					return;
				}else{
					matrix[row][column] = null;
				}
			}
		}
	}
	
	public static boolean isfit(Piece piece, int row, int column){
		Piece upper = matrix[row - 1][column];
		Piece left = matrix[row][column - 1];
		Piece bottom =  matrix[row + 1][column];
		Piece right = matrix[row][column + 1];
		
		if(upper != null && !piece.up.equals(upper.down)){
			return false;
		}
		if(left != null && !piece.left.equals(left.right)){
			return false;
		}
		if(bottom != null && !piece.down.equals(bottom.up)){
			return false;
		}
		if(right != null && !piece.right.equals(right.left)){
			return false;
		}
		return true;
	}

}

class Piece{
	public String up;
	public String left;
	public String down;
	public String right;

	public Piece(String up, String left, String down, String right){
		this.up = up;
		this.left = left;
		this.down = down;
		this.right = right;
	}

	public Piece(String color){
		up = color;
		left = color;
		right = color;
		down = color;
	}

	@Override
	public String toString(){
		return '(' + up + ',' + left + ',' + down + ',' + right + ')';
	}

	public static Piece getPiece(String data){
		int length = data.length();
		int ini = 1, j = 0;
		String[] colors = data.split(",");

		return new Piece(
				colors[0].substring(1), 
				colors[1], 
				colors[2], 
				colors[3].substring(0,colors[3].length() - 1));
	}
}