import java.io.*;
import java.util.*;

public class CircuitDesign {
    private final InputReader reader;
    private final OutputWriter writer;

    public CircuitDesign(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
		new Thread(null, new Runnable() {
                    public void run() {
                        
							InputReader reader = new InputReader(System.in);
							OutputWriter writer = new OutputWriter(System.out);
                            new CircuitDesign(reader, writer).run();
							writer.writer.flush();
                    }
                }, "1", 1 << 26).start();
    }

    class Clause {
        int firstVar;
        int secondVar;
    }

    class TwoSatisfiability {
        int numVars;
        Clause[] clauses;

        TwoSatisfiability(int n, int m) {
            numVars = n;
            clauses = new Clause[m];
            for (int i = 0; i < m; ++i) {
                clauses[i] = new Clause();
            }
        }

        boolean isSatisfiable(int[] result) {
            // This solution tries all possible 2^n variable assignments.
            // It is too slow to pass the problem.
            // Implement a more efficient algorithm here.
			ArrayList<Integer>[] adjList = new ArrayList[2 * numVars];
			ArrayList<Integer>[] reverseAdj = new ArrayList[2 * numVars];
			
			for (int i = 0; i < adjList.length; i++) {
				adjList[i] = new ArrayList<Integer>();
				reverseAdj[i] = new ArrayList<Integer>();
			}
			
            for(int i = 0; i < clauses.length; ++i){
				Clause c = clauses[i];
				int L1, L2, notL1, notL2;
				L1 = assignVertex(c.firstVar);
				notL1 = negate(L1);
				L2 = assignVertex(c.secondVar);
				notL2 = negate(L2);
				
				adjList[notL1].add(L2);    //Edge L1' ---> L2
				adjList[notL2].add(L1);    //Edge L2' ---> L1
				//reverse graph
				reverseAdj[L2].add(notL1);
				reverseAdj[L1].add(notL2);
			}
			Graph graph = new Graph(adjList);
			Graph reverseGraph = new Graph(reverseAdj);
			
			reverseGraph.traverseDFS();
			int numSCC = 0;
			ArrayList<Integer> order = reverseGraph.toposort();
			
			for(int i = order.size() - 1; i >= 0; --i){
				//System.out.print(order.get(i) + " ");
				int v = order.get(i);
				if(!graph.visited(v)){
					graph.explore(v, numSCC);
					numSCC++;
				}
			}
			
			//System.out.println("\nnumSCC = " + numSCC);
			
			int[] SCC = graph.getSCC();
			for(int i = 0; i < numVars; ++i){
				if(SCC[i] == SCC[negate(i)])
					return false;
			}
			
			ArrayList<Integer>[] set = new ArrayList[numSCC];
			for(int i = 0; i < numSCC; ++i){
				set[i] = new ArrayList<Integer>();
			}
			for(int i = 0; i < SCC.length; ++i){
				set[SCC[i]].add(i);
				//System.out.print(SCC[i] + " ");
			}
			
			boolean[] assigned = new boolean[SCC.length];
			
			Arrays.fill(assigned, false);
			int[] ans = new int[SCC.length];
			
			for(int i = 0; i < numSCC; ++i){
				ArrayList<Integer> vertices = set[i];
				for(int j = 0; j < vertices.size(); ++j){
					if(!assigned[vertices.get(j)]){
						ans[vertices.get(j)] = 1;
						ans[negate(vertices.get(j))] = 0;
						assigned[vertices.get(j)] = true;
						assigned[negate(vertices.get(j))] = true;
						//System.out.print(vertices.get(j) + " ");
					}
					//System.out.print(vertices.get(j) + " ");
				}
				//System.out.println();
			}
			
			for(int i = 0; i < numVars; ++i){
				result[i] = ans[i];
			}
			
            return true;
        }
		
		int negate(int num){
			if(num < numVars){
				return num + numVars;
			}else{
				return num - numVars;
			}
		}
		
		int assignVertex(int num){
			if(num > 0){
				return num - 1;
			}else {
				return numVars - num - 1;
			}
			
		}
    }
	
    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        TwoSatisfiability twoSat = new TwoSatisfiability(n, m);
        for (int i = 0; i < m; ++i) {
            twoSat.clauses[i].firstVar = reader.nextInt();
            twoSat.clauses[i].secondVar = reader.nextInt();
        }

        int result[] = new int[n];
        if (twoSat.isSatisfiable(result)) {
            writer.printf("SATISFIABLE\n");
            for (int i = 1; i <= n; ++i) {
                if (result[i-1] == 0) {
                    writer.printf("%d", -i);
                } else {
                    writer.printf("%d", i);
                }
                if (i < n) {
                    writer.printf(" ");
                } else {
                    writer.printf("\n");
                }
            }
        } else {
            writer.printf("UNSATISFIABLE\n");
        }
    }
	
	class Graph {
	private ArrayList<Integer>[] adjList = null;
	private boolean visited[];
	private ArrayList<Integer> result;
	private int[] SCC = null;
	
	public Graph(ArrayList<Integer>[] adjList){
		this.adjList = adjList;
		visited = new boolean[adjList.length];
		
		Arrays.fill(visited, false);
		SCC = new int[adjList.length];
		Arrays.fill(SCC, -1);
		result = new ArrayList<Integer>(adjList.length);
	}
	
	public void traverseDFS(){
		for(int i = 0; i < adjList.length; ++i){
			if(!visited(i)){
				explore(i, -1);
				postvisit(i);
			}
		}
	}
	
	public int[] getSCC(){
		return SCC;
	}
	
	public ArrayList<Integer> toposort(){
		return result;
	}
	
	private void postvisit(int vertex){
		result.add(vertex);
	}
	
	public boolean visited(int vertex){
		return visited[vertex];
	}
	
	public void explore(int vertex, int scc){
		visited[vertex] = true;
		SCC[vertex] = scc;
		ArrayList<Integer> neighbours = adjList[vertex];
		
		for(int i = 0; i < neighbours.size(); ++i){
			int v = neighbours.get(i);
			if(!visited(v)){
				explore(v, scc);
				postvisit(v);
			}
		}
	}
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