import java.io.*;
import java.util.*;

public class StockCharts {
    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new StockCharts().solve();
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        int[][] stockData = readData();
        int result = minCharts(stockData);
        writeResponse(result);
        out.close();
    }

    int[][] readData() throws IOException {
        int numStocks = in.nextInt();
        int numPoints = in.nextInt();
        int[][] stockData = new int[numStocks][numPoints];
        for (int i = 0; i < numStocks; ++i)
            for (int j = 0; j < numPoints; ++j)
                stockData[i][j] = in.nextInt();
        return stockData;
    }

    private int minCharts(int[][] stockData) {
        // Replace this incorrect greedy algorithm with an
        // algorithm that correctly finds the minimum number
        // of charts on which we can put all the stock data
        // without intersections of graphs on one chart.
		
		int numStocks = stockData.length;
		boolean[][] mat = buildMatrix(stockData);
		
		return numStocks - findMatching(mat);
    }
	
	private boolean[][] buildMatrix(int[][] stockData){
		boolean[][] mat = new boolean[stockData.length][stockData.length];
		//System.out.println("mat[][] : ");
		for(int i = 0; i < stockData.length; ++i){
			for(int j = 0; j < stockData.length; ++j){
				mat[i][j] = compare(stockData[i], stockData[j]);
				//System.out.print(mat[i][j] + " ");
			}
			//System.out.println();
		}
		return mat;
	}

    boolean compare(int[] stock1, int[] stock2) {
        for (int i = 0; i < stock1.length; ++i)
            if (stock1[i] >= stock2[i])
                return false;
        return true;
    }
	
	private int findMatching(boolean[][] bipartiteGraph) {
        // Replace this code with an algorithm that finds the maximum
        // matching correctly in all cases.
        int numLeft = bipartiteGraph.length;
        int numRight = bipartiteGraph[0].length;

		//System.out.println("row column : " + numLeft + " " + numRight);
		FlowGraph graph = buildNetwork(bipartiteGraph);
		return maxFlow(graph, 0, numLeft + numRight + 1);
    }
	
	private FlowGraph buildNetwork(boolean[][] adjMatrix){
		int row = adjMatrix.length;
		int column = adjMatrix[0].length;
		FlowGraph graph = new FlowGraph(row + column + 2);
		
		//System.out.println("row column : " + row + " " + column);
		
		for(int i = 0; i < row; ++i){
			graph.addEdge(0, i + 1, 1);
		}
		for(int i = 0; i < column; ++i){
			graph.addEdge(i + row + 1, row + column + 1, 1);
		}
		for(int i = 0; i < row; ++i){
			for(int j = 0; j < column; ++j){
				if(adjMatrix[i][j]){
					graph.addEdge(i + 1, j + row + 1, 1);
				}
			}
		}
		
		return graph;
	}
	
	private static int maxFlow(FlowGraph graph, int from, int to) {
        int flow = 0;
        /* your code goes here */
		graph.traverseBFS(from);
		while(graph.getMinCapacity(to) != graph.INFINITY){
			flow += graph.getMinCapacity(to);
			graph.updateGraph(from, to, graph.getMinCapacity(to));
			graph.traverseBFS(from);
		}
		
        return flow;
    }

    static class Edge {
        int from, to, capacity, flow;

        public Edge(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
        }
    }

    /* This class implements a bit unusual scheme to store the graph edges, in order
     * to retrieve the backward edge for a given edge quickly. */
    static class FlowGraph {
        /* List of all - forward and backward - edges */
        private List<Edge> edges;

        /* These adjacency lists store only indices of edges from the edges list */
        private List<Integer>[] graph;
		
		int INFINITY = -1;
		int[] capacity;
		int[] previous;
		private Queue<Integer> queue;

        public FlowGraph(int n) {
            this.graph = (ArrayList<Integer>[])new ArrayList[n];
            for (int i = 0; i < n; ++i)
                this.graph[i] = new ArrayList<>();
            this.edges = new ArrayList<>();
			capacity = new int[n];
			previous = new int[n];
        }

        public void addEdge(int from, int to, int capacity) {
            /* Note that we first append a forward edge and then a backward edge,
             * so all forward edges are stored at even indices (starting from 0),
             * whereas backward edges are stored at odd indices. */
            Edge forwardEdge = new Edge(from, to, capacity);
            Edge backwardEdge = new Edge(to, from, 0);
            graph[from].add(edges.size());
            edges.add(forwardEdge);
            graph[to].add(edges.size());
            edges.add(backwardEdge);
        }

        public int size() {
            return graph.length;
        }

        public List<Integer> getIds(int from) {
            return graph[from];
        }

        public Edge getEdge(int id) {
            return edges.get(id);
        }
		
		public int getTotalEdges(){
			return edges.size();
		}

        public void addFlow(int id, int flow) {
            /* To get a backward edge for a true forward edge (i.e id is even), we should get id + 1
             * due to the described above scheme. On the other hand, when we have to get a "backward"
             * edge for a backward edge (i.e. get a forward edge for backward - id is odd), id - 1
             * should be taken.
             *
             * It turns out that id ^ 1 works for both cases. Think this through! */
            edges.get(id).capacity -= flow;
            edges.get(id ^ 1).capacity += flow;
        }
		
		private static final int MAX = 10005;
		public void traverseBFS(int origin){
			for(int i = 0; i < size(); ++i){
				previous[i] = capacity[i] = INFINITY;
			}
			capacity[origin] = MAX;
			queue = new LinkedList<Integer>();
			queue.add(origin);
			List<Integer> neighbours = null;
			while(queue.peek() != null){
				int vertex = queue.poll();
				neighbours = getIds(vertex);
				for(int i = 0; i < neighbours.size(); ++i){
					int id = neighbours.get(i);
					Edge e = getEdge(id);
					if(e.capacity != 0 && capacity[e.to] == INFINITY){
						queue.offer(e.to);
						capacity[e.to] = (capacity[vertex] > e.capacity) ? e.capacity : capacity[vertex];
						previous[e.to] = id;
					}
				}
			}
		}
		
		public int getMinCapacity(int vertex){
			return capacity[vertex];
		}
		
		public void updateGraph(int source, int sink, int flow){
			int vertex = sink;
			while(vertex != source){
				int id = previous[vertex];
				addFlow(id, flow);
				vertex = edges.get(id).from;
			}
		}
    }

    private void writeResponse(int result) {
        out.println(result);
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
