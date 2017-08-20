import java.util.ArrayList;
import java.util.Scanner;

public class NegativeCycle {
    private static int negativeCycle(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost) {
        // write your code here
		WeightedGraph g = new WeightedGraph(adj, cost);
		for(int i = 0; i < adj.length; ++i){
			if(!g.visited(i)){
				if(g.bellmanFord(i))
					return 1;
			}
		}
        return 0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
        ArrayList<Integer>[] cost = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y, w;
            x = scanner.nextInt();
            y = scanner.nextInt();
            w = scanner.nextInt();
            adj[x - 1].add(y - 1);
            cost[x - 1].add(w);
        }
        System.out.println(negativeCycle(adj, cost));
    }
}

class WeightedGraph {
	private ArrayList<Integer>[] adjList;
	private ArrayList<Integer>[] weights;
	private int[] dist;
	public int INF;
	private boolean[] visited;
	
	public WeightedGraph(ArrayList<Integer>[] adj, ArrayList<Integer>[] weights){
		adjList = adj;
		this.weights = weights;
		dist = new int[adjList.length];
		INF = Integer.MAX_VALUE;
		visited = new boolean[adjList.length];
	}
	
	public boolean bellmanFord(int origin){
		for(int i = 0; i < dist.length; ++i){
			dist[i] = INF;
		}
		dist[origin] = 0;
		visited[origin] = true;
		
		ArrayList<Integer> neighbours;
		ArrayList<Integer> neighWeights;
		boolean isRelaxed = false;
		
		for(int i = 0; i < adjList.length; ++i){
			isRelaxed = false;
			for(int j = 0; j < adjList.length; ++j){
				neighbours = adjList[j];
				neighWeights = weights[j];
				for(int k = 0; k < neighbours.size(); ++k){
					if(relax(j, neighbours.get(k), neighWeights.get(k)))
						isRelaxed = true;
				}
			}
			if(!isRelaxed){
				return isRelaxed;
			}
		}
		
		return isRelaxed;
	}
	
	public boolean visited(int vertex){
		return visited[vertex];
	}
	
	private boolean relax(int ini, int fin, int weight){
		if(dist[ini] == INF)
			return false;
		if(dist[fin] > dist[ini] + weight){
			dist[fin] = dist[ini] + weight;
			visited[fin] = true;
			return true;
		}
		return false;
	}
}