import java.util.*;

public class ShortestPaths {

    private static long[] shortestPaths(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s) {
		//write your code here
		WeightedGraph g = new WeightedGraph(adj, cost);
		g.bellmanFord(s);
		g.traverseBFS();
		long[] result = g.getDistances();
		return result;
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
        int s = scanner.nextInt() - 1;
		long[] result;
        result = shortestPaths(adj, cost, s);
        for (int i = 0; i < n; i++) {
            if (result[i] == Long.MAX_VALUE) {
                System.out.println('*');
            } else if (result[i] == -1) {
                System.out.println('-');
            } else {
                System.out.println(result[i]);
            }
        }
    }

}

class WeightedGraph {
	private ArrayList<Integer>[] adjList;
	private ArrayList<Integer>[] weights;
	private long[] dist;
	public long INF;
	private Queue<Integer> queue;
	
	public WeightedGraph(ArrayList<Integer>[] adj, ArrayList<Integer>[] weights){
		adjList = adj;
		this.weights = weights;
		dist = new long[adjList.length];
		INF = Long.MAX_VALUE;
		queue = new LinkedList<Integer>();
	}
	
	public void bellmanFord(int origin){
		for(int i = 0; i < dist.length; ++i){
			dist[i] = INF;
		}
		dist[origin] = 0;
		
		ArrayList<Integer> neighbours;
		ArrayList<Integer> neighWeights;
		boolean isRelaxed = false;
		
		for(int i = 0; i < adjList.length - 1; ++i){
			isRelaxed = false;
			for(int j = 0; j < adjList.length; ++j){
				neighbours = adjList[j];
				neighWeights = weights[j];
				for(int k = 0; k < neighbours.size(); ++k){
					if(relax(j, neighbours.get(k), neighWeights.get(k)))
						isRelaxed = true;
				}
			}
			if(!isRelaxed) return;
		}
		//v-th iteration
		for(int j = 0; j < adjList.length; ++j){
				neighbours = adjList[j];
				neighWeights = weights[j];
				for(int k = 0; k < neighbours.size(); ++k){
					if(relax(j, neighbours.get(k), neighWeights.get(k))){
						if(dist[neighbours.get(k)] != -1){
							dist[neighbours.get(k)] = -1;
							queue.add(neighbours.get(k));
						}
					}
				}
			}
	}
	
	public void traverseBFS(){
		if(queue.isEmpty())
			return;
		ArrayList<Integer> neighbours;
		while(!queue.isEmpty()){
			int vertex = queue.remove();
			neighbours = adjList[vertex];
			for(int i = 0; i < neighbours.size(); ++i){
				int v = neighbours.get(i);
				if(dist[v] != -1){
					dist[v] = -1;
					queue.add(v);
				}
			}
		}
	}
	
	public long[] getDistances(){
		return dist;
	}
	
	private boolean relax(int ini, int fin, int weight){
		if(dist[ini] == INF)
			return false;
		if(dist[fin] > dist[ini] + weight){
			dist[fin] = dist[ini] + weight;
			return true;
		}
		return false;
	}
}