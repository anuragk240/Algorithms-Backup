import java.io.*;
import java.util.*;

public class CleaningApartment {
    private final InputReader reader;
    private final OutputWriter writer;

    public CleaningApartment(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new CleaningApartment(reader, writer).run();
        writer.writer.flush();
    }

    class Edge {
        int from;
        int to;
    }

    class ConvertHampathToSat {
        int numVertices;
        Edge[] edges;

        ConvertHampathToSat(int n, int m) {
            numVertices = n;
            edges = new Edge[m];
            for (int i = 0; i < m; ++i) {
                edges[i] = new Edge();
            }
        }

        void printEquisatisfiableSatFormula() {
            // This solution prints a simple satisfiable formula
            // and passes about half of the tests.
            // Change this function to solve the problem.
			int variables = numVertices * numVertices;
			int clauses = (1 + (numVertices * (numVertices - 1)) / 2);
			clauses *= numVertices;
			clauses *= 2;

			boolean[][] mat = new boolean[numVertices][numVertices];
			for(int i = 0; i < numVertices; ++i){
				Arrays.fill(mat[i], false);
			}
			
			for(int i = 0; i < edges.length; ++i){
				mat[edges[i].to - 1][edges[i].from - 1] = true;
				mat[edges[i].from - 1][edges[i].to - 1] = true;
			}
			ArrayList<Edge> e = new ArrayList<Edge>();
			for(int i = 0; i < numVertices; ++i){
				for(int j = i + 1; j < numVertices; ++j){
					if(!mat[i][j]){
						Edge t = new Edge();
						t.to = i + 1;
						t.from = j + 1;
						e.add(t);
					}
				}
			}
			
			clauses +=  (numVertices - 1) * 2 * e.size(); 
			
			writer.printf("%d %d\n", clauses, variables);
			for(int i = 0; i < numVertices; ++i){
				for(int j = 0; j < numVertices; ++j){
					int ij = getnum(i + 1, j + 1);
					writer.printf("%d ", ij);
				}
				writer.printf("0\n");
				for(int j = 0; j < numVertices; ++j){
					for(int k = j + 1; k < numVertices; ++k){
						writer.printf("%d %d 0\n", -getnum(i + 1, j + 1), -getnum(i + 1, k + 1));
					}
				}
			}
			
			for(int i = 0; i < numVertices; ++i){
				for(int j = 0; j < numVertices; ++j){
					int ij = getnum(j + 1, i + 1);
					writer.printf("%d ", ij);
				}
				writer.printf("0\n");
				for(int j = 0; j < numVertices; ++j){
					for(int k = j + 1; k < numVertices; ++k){
						writer.printf("%d %d 0\n", -getnum(j + 1, i + 1), -getnum(k + 1, i + 1));
					}
				}
			}
			
			for(int i = 0; i < e.size(); ++i){
				for(int j = 1; j < numVertices; ++j){
					writer.printf("%d %d 0\n", -getnum(e.get(i).to, j + 1), -getnum(e.get(i).from, j));
					writer.printf("%d %d 0\n", -getnum(e.get(i).to, j), -getnum(e.get(i).from, j + 1));
				}
			}
			
        }
		
		int getnum(int i, int j){
			return (i - 1) * numVertices + j;
		}
    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        ConvertHampathToSat converter = new ConvertHampathToSat(n, m);
        for (int i = 0; i < m; ++i) {
            converter.edges[i].from = reader.nextInt();
            converter.edges[i].to = reader.nextInt();
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
