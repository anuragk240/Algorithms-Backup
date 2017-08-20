import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.StringTokenizer;

public class SchoolBus {
    private static FastScanner in;
    private static int INF = 1000 * 1000 * 1000;

    public static void main(String[] args) {
        in = new FastScanner();
        try {
            printAnswer(SchoolBus(readData()));
        } catch (IOException exception) {
            System.err.print("Error during reading: " + exception.toString());
        }
    }

    static Answer SchoolBus(int[][] graph) {
        // This solution tries all the possible sequences of stops.
        // It is too slow to pass the problem.
        // Implement a more efficient algorithm here.
        int n = graph.length;
        Integer[] p = new Integer[n];
        for (int i = 0; i < n; ++i)
            p[i] = i;

        class Solver {
            int bestAnswer = INF;
            List<Integer> bestPath;

            public void solve(Integer[] a, int n) {
                int count = (int) Math.pow(2, n);
				int[][] data = new int[count][n];
				for(int i = 0; i < count; ++i){
					Arrays.fill(data[i], INF);
				}
				data[1][0] = 0;
				int[][][] cycle = new int[count][n][n];
				cycle[1][0][0] = 0;
				
				for(int i = 1; i < n; ++i){
					cycle[1][0][0] = 0;
				}

				for(int i = 1; i < count; i += 2){
					//System.out.println("i = " + Integer.toBinaryString(i));
					for(int j = 1; j < n; ++j){
						if(getBit(j, i) == 1){
							//System.out.println("j = " + j);
							int min = INF;
							int set = setBit(j, i), c = 0, length = 0;
							//System.out.println("set = " + Integer.toBinaryString(set)); 
							for(int k = 0; k < n; ++k){
								if(getBit(k, set) == 1){
									//System.out.println("K= " + k);
									//System.out.println("data[set][k] + graph[k][j] < min : " + data[set][k] + "+" + graph[k][j] + "<" + min);
									if(data[set][k] + graph[k][j] < min){
										min = data[set][k] + graph[k][j];
										c = k;
									}
									length++;
									//System.out.println("c = " + c);
								}
							}
							data[i][j] = min;
							for(int k = 0; k < length; ++k){
								cycle[i][j][k] = cycle[set][c][k];
							}
							cycle[i][j][length] = j;
						}
					}
				}
				int min = INF;
				int index = 1;
				for(int i = 1; i < n; ++i){
					if(data[count - 1][i] + graph[i][0] < min){
						min = data[count - 1][i] + graph[i][0];
						index = i;
					}
				}
				
				// System.out.println("cycle:");
				// for(int i = 1; i < count; i+=2){
					// for(int j = 0; j < n; ++j){
						// for(int k = 0; k < n; ++k){
							// System.out.print(cycle[i][j][k]+ ",");
						// }
						// System.out.print("\t");
					// }
					// System.out.println();
				// }
				
				// System.out.println("data:");
				// for(int i = 1; i < count; i+=2){
					// for(int j = 0; j < n; ++j){
						// System.out.print(data[i][j]+ " ");
					// }
					// System.out.println();
				// }
				bestAnswer = min;
				bestPath = new ArrayList<Integer>();
				for(int i = 0; i < n; ++i){
					bestPath.add(cycle[count - 1][index][i] + 1);
				}
            }

            private void swap(Integer[] a, int i, int j) {
                int tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
            }
			
			public int getBit(int position, int num) {
				return (num >> (position)) & 1;
			}
			
			public int setBit(int position, int num){
				return (int) Math.pow(2, position) ^ num;
			}
        }
        Solver solver = new Solver();
        solver.solve(p, n);
        if (solver.bestAnswer == INF)
            return new Answer(-1, new ArrayList<>());
        List<Integer> bestPath = solver.bestPath;
        return new Answer(solver.bestAnswer, bestPath);
    }

    private static int[][] readData() throws IOException {
        int n = in.nextInt();
        int m = in.nextInt();
        int[][] graph = new int[n][n];

        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                graph[i][j] = INF;

        for (int i = 0; i < m; ++i) {
            int u = in.nextInt() - 1;
            int v = in.nextInt() - 1;
            int weight = in.nextInt();
            graph[u][v] = graph[v][u] = weight;
        }
        return graph;
    }

    private static void printAnswer(final Answer answer) {
        System.out.println(answer.weight);
        if (answer.weight == -1)
            return;
        for (int x : answer.path)
            System.out.print(x + " ");
        System.out.println();
    }

    static class Answer {
        int weight;
        List<Integer> path;

        public Answer(int weight, List<Integer> path) {
            this.weight = weight;
            this.path = path;
        }
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

}
