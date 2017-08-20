import java.io.*;
import java.util.*;

public class BudgetAllocation {
    private final InputReader reader;
    private final OutputWriter writer;

    public BudgetAllocation(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new BudgetAllocation(reader, writer).run();
        writer.writer.flush();
    }

    class ConvertILPToSat {
        int[][] A;
        int[] b;

        ConvertILPToSat(int n, int m) {
            A = new int[n][m];
            b = new int[n];
        }

        void printEquisatisfiableSatFormula() {
            // This solution prints a simple satisfiable formula
            // and passes about half of the tests.
            // Change this function to solve the problem.
			ArrayList<String> clauses = new ArrayList<String>();
			int[] soln = new int[3];
			for(int i = 0; i < A.length; ++i){
				int count = 0;
				for(int j = 0; j < A[0].length; ++j){
					if(A[i][j] != 0){
						soln[count] = j;
						count++;
					}
				}
				int c = (int)Math.pow(2, count);
				for(int j = 0; j < c; ++j){
					int sum = 0;
					for(int k = 0; k < count; ++k){
						//writer.printf("%d ",getBit(k, j));
						if(getBit(k, j) == 1){
							sum += A[i][soln[k]];
						}
					}
					//writer.printf("j= %s\n", Integer.toBinaryString(j));
					if(sum > b[i]){
						int temp = j;
						temp = ~temp;
						String claus = "";
						//writer.printf("~j= %s\n", Integer.toBinaryString(temp));
						for(int k = 0; k < count; ++k){
							if(getBit(k, temp) == 1)
								claus = claus + (soln[k] + 1) + " ";
							else
								claus = claus + -(soln[k] + 1) + " ";
						}
						claus = claus + "0\n";
						clauses.add(claus);
					}
				}
			}
			
			if(clauses.size() == 0){
				writer.printf("1 1\n");
				writer.printf("1 -1 0\n");
				return;
			}
			writer.printf("%d %d\n", clauses.size(), A[0].length);
			for(int i = 0; i < clauses.size(); ++i){
				writer.printf(clauses.get(i));
			}
        }
    }
	
	public int getBit(int position, int num) {
		return (num >> (position)) & 1;
	}

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        ConvertILPToSat converter = new ConvertILPToSat(n, m);
        for (int i = 0; i < n; ++i) {
          for (int j = 0; j < m; ++j) {
            converter.A[i][j] = reader.nextInt();
          }
        }
        for (int i = 0; i < n; ++i) {
            converter.b[i] = reader.nextInt();
        }

        converter.printEquisatisfiableSatFormula();
    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    static class OutputWriter {
        public PrintWriter writer;

        OutputWriter(OutputStream stream) {
            writer = new PrintWriter(stream);
        }

        public void printf(String format, Object... args) {
            writer.print(String.format(Locale.ENGLISH, format, args));
        }
    }
}
