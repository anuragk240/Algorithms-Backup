import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class Equation {
    Equation(double a[][], double b[]) {
        this.a = a;
        this.b = b;
    }

    double a[][];
    double b[];
}

class Position {
    Position(int column, int row) {
        this.column = column;
        this.row = row;
    }

    int column;
    int row;
}

class EnergyValues {
    static Equation ReadEquation() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();

        double a[][] = new double[size][size];
        double b[] = new double[size];
        for (int row = 0; row < size; ++row) {
            for (int column = 0; column < size; ++column)
                a[row][column] = scanner.nextInt();
            b[row] = scanner.nextInt();
        }
        return new Equation(a, b);
    }

    static Position SelectPivotElement(Position pivot_element, double a[][], double b[]) {
        // This algorithm selects the first free element.
        // You'll need to improve it to pass the problem.
		int last_row = pivot_element.row;
		for(int j = pivot_element.column;j < a.length; ++j){   //a is a square matrix
			for(int i = pivot_element.row;i < a.length; ++i){
				if(a[i][j] != 0){
					//swapping
					double temp;
					for(int k = 0; k < a.length; ++k){
						temp = a[i][k];
						a[i][k] = a[last_row][k];
						a[last_row][k] = temp;
					}
					
					temp = b[i];
					b[i] = b[last_row];
					b[last_row] = temp;
					
					pivot_element.column = j;
					pivot_element.row = last_row;
					return pivot_element;
				}
			}
		}
		
		return null;
    }

    static void ProcessPivotElement(double a[][], double b[], Position pivot_element) {
        // Write your code here
		scale(1.0 / a[pivot_element.row][pivot_element.column], a, b, pivot_element);
		transform(a, b, pivot_element);
    }
	
	private static void scale(double num, double[][] a, double[] b, Position pivot){
		int row = pivot.row;
		for(int i = 0; i < a.length; ++i){  //a is a square matrix
			a[row][i] *= num;
		}
		a[row][pivot.column] = 1;
		b[row] *= num;
	}
	
	private static void transform(double[][] a, double[] b, Position pivot){
		int row = pivot.row;
		int column = pivot.column;
		
		for(int i = 0; i < a.length; ++i){
			if(i == row) continue;
			double num = (-1.0) * a[i][column];
			b[i] = b[row] * num + b[i];
			for(int j = 0; j < a.length; ++j){
				a[i][j] = a[row][j] * num + a[i][j];
			}
			a[i][column] = 0;
		}
	}

    static double[] SolveEquation(Equation equation) {
        double a[][] = equation.a;
        double b[] = equation.b;
        int size = a.length;

		Position pivot_element = new Position(0,0);
		
        for (int step = 0; step < size; ++step) {
			pivot_element = SelectPivotElement(pivot_element, a, b);
			//System.out.println("pivot= " + pivot_element.row + "," + pivot_element.column);
            ProcessPivotElement(a, b, pivot_element);
            pivot_element.row++;
			pivot_element.column++;
        }

        return b;
    }

    static void PrintColumn(double column[]) {
        int size = column.length;
        for (int row = 0; row < size; ++row)
            System.out.printf("%.20f\n", column[row]);
    }

    public static void main(String[] args) throws IOException {
        Equation equation = ReadEquation();
        double[] solution = SolveEquation(equation);
        PrintColumn(solution);
    }
}
