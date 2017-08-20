import java.util.*;
import java.io.*;

public class RescheduleExams {

    class Edge {
        int u, v;
        public Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }
	

    class ConvertGSMNetworkProblemToSat {
        int numVertices;
        Edge[] edges;

        ConvertGSMNetworkProblemToSat (int n, int m) {
            numVertices = n;
            edges = new Edge[m];
        }

        ArrayList<Clause> getEquisatisfiableSatFormula(char[] colors) {
            // This solution prints a simple satisfiable formula
            // and passes about half of the tests.
            // Change this function to solve the problem.
			int variable = 3 * numVertices;
			ArrayList<Clause> clause = new ArrayList<Clause>();
			
			for(int i = 0; i < numVertices; ++i){
				int j = mapColorToInt(colors[i]);
				int ij = getnum(i + 1, j);
				int i1 = getnum(i + 1, 1), i2 = i1 + 1, i3 = i2 + 1;
				if(i1 == ij){
					clause.add(new Clause(-i2, -i3));
					clause.add(new Clause(i2, i3));
				}else if(i2 == ij){
					clause.add(new Clause(-i1, -i3));
					clause.add(new Clause(i1, i3));
				}else{
					clause.add(new Clause(-i2, -i1));
					clause.add(new Clause(i2, i1));
				}
				clause.add(new Clause(-ij, -ij));
			}
			boolean[] b = new boolean[3];
			for(int x = 0; x < edges.length; ++x){
				Arrays.fill(b, true);
				int i = getnum(edges[x].u, 1);
				int j = getnum(edges[x].v, 1);
				int ic = getnum(edges[x].u, mapColorToInt(colors[edges[x].u - 1]));
				int jc = getnum(edges[x].v, mapColorToInt(colors[edges[x].v - 1]));
				if(ic == i || jc == j){
					b[0] = false;
				}
				if(ic == i + 1 || jc == j + 1){
					b[1] = false;
				}
				if(ic == i + 2 || jc == j + 2){
					b[2] = false;
				}
				for(int k = 0; k < 3; ++k){
					if(b[k])
						clause.add(new Clause(-(i + k), -(j + k)));
				}
			}
			// System.out.println("clauses");
			// for(int i = 0; i < clause.size(); ++i){
				// System.out.println(clause.get(i).firstVar + "," + clause.get(i).secondVar);
			// }
			return clause;
        }
		
		int getnum(int i, int j){
			return (i - 1) * 3 + j;
		}
		
		int mapColorToInt(char c){
			switch(c){
				case 'R':
					return 1;
				case 'G':
					return 2;
				case 'B':
					return 3;
				default:
					return -1;
			}
		}
    }

    char[] assignNewColors(int n, ArrayList<Edge> edges, char[] colors) {
        // Insert your code here.
		ConvertGSMNetworkProblemToSat sat = new ConvertGSMNetworkProblemToSat(n, edges.size());
		sat.edges = edges.toArray(new Edge[edges.size()]);
		ArrayList<Clause> clauses = sat.getEquisatisfiableSatFormula(colors);
		
		TwoSatisfiability twosat = new TwoSatisfiability(n * 3, clauses.size());
		twosat.clauses = clauses.toArray(new Clause[clauses.size()]);
		
		int[] result = new int[n * 3];
		boolean satisfiable = twosat.isSatisfiable(result);
		
		if(!satisfiable) return null;
		
		char[] newcolors = new char[n];
		
		for(int i = 0; i < n; ++i){
			int i1 = sat.getnum(i + 1, 1) - 1;
			if(result[i1] == 1){
				newcolors[i] = 'R';
			}else if(result[i1 + 1] == 1){
				newcolors[i] = 'G';
			}else{
				newcolors[i] = 'B';
			}
		}
		return newcolors;
    }
	
	class Clause {
        int firstVar;
        int secondVar;
		
		public Clause(int n, int m){
			firstVar = n;
			secondVar = m;
		}
    }

    class TwoSatisfiability {
        int numVars;
        Clause[] clauses;

        TwoSatisfiability(int n, int m) {
            numVars = n;
            clauses = new Clause[m];
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

    void run() {
        Scanner scanner = new Scanner(System.in);
        PrintWriter writer = new PrintWriter(System.out);

        int n = scanner.nextInt();
        int m = scanner.nextInt();
        scanner.nextLine();
        
        String colorsLine = scanner.nextLine();
        char[] colors = colorsLine.toCharArray();

        ArrayList<Edge> edges = new ArrayList<Edge>();
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
			if (u != v){
				edges.add(new Edge(u, v));
			}
        }

        char[] newColors = assignNewColors(n, edges, colors);

        if (newColors == null) {
            writer.println("Impossible");
        } else {
            writer.println(new String(newColors));
        }

        writer.close();
    }

	public static void main(String[] args) {
		new Thread(null, new Runnable() {
                    public void run() {
						new RescheduleExams().run();
                    }
                }, "1", 1 << 26).start();
    }
}
