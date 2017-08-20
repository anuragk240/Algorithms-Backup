import java.io.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.StringTokenizer;

public class GSMNetwork {
    private final InputReader reader;
    private final OutputWriter writer;

    public GSMNetwork(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new GSMNetwork(reader, writer).run();
        writer.writer.flush();
    }

    class Edge {
        int from;
        int to;
    }

    class ConvertGSMNetworkProblemToSat {
        int numVertices;
        Edge[] edges;

        ConvertGSMNetworkProblemToSat (int n, int m) {
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
			int variable = 3 * numVertices;
			int clauses = 3 * edges.length + 4 * numVertices;
			writer.printf("%d %d\n", clauses, variable);
			for(int i = 0; i < numVertices; ++i){
				int i1 = getnum(i + 1, 1), i2 = i1 + 1, i3 = i2 + 1;
				writer.printf("%d %d %d 0\n", i1, i2, i3);
				writer.printf("%d %d 0\n", -i1, -i2);
				writer.printf("%d %d 0\n", -i2, -i3);
				writer.printf("%d %d 0\n", -i1, -i3);
			}
			
			for(int i = 0; i < edges.length; ++i){
				int i1 = getnum(edges[i].to, 1), i2 = i1 + 1, i3 = i2 + 1;
				int j1 = getnum(edges[i].from, 1), j2 = j1 + 1, j3 = j2 + 1;
				writer.printf("%d %d 0\n", -i1, -j1);
				writer.printf("%d %d 0\n", -i2, -j2);
				writer.printf("%d %d 0\n", -i3, -j3);
			}
        }
		
		int getnum(int i, int j){
			return (i - 1) * 3 + j;
		}
    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        ConvertGSMNetworkProblemToSat  converter = new ConvertGSMNetworkProblemToSat (n, m);
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
