import java.util.*;

public class Dijkstra {
    private static int distance(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s, int t) {
		WeightedGraph g = new WeightedGraph(adj, cost);
		g.dijkstra(s);
		int answer = g.getMinDistance(t);
		if(answer == g.INF)
			return -1;
		else return answer;
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
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        System.out.println(distance(adj, cost, x, y));
    }
}

class WeightedGraph {
	private ArrayList<Integer>[] adjList;
	private ArrayList<Integer>[] weights;
	private class Node {
		int index;
		int distance;
		
		public Node(int i, int dist){
			index = i;
			distance = dist;
		}
	}
	private Comparator comparator;
	private PriorityQueue<Node> queue;
	private int[] dist;
	private boolean[] processed;
	public int INF;
	
	public WeightedGraph(ArrayList<Integer>[] adj, ArrayList<Integer>[] weights){
		adjList = adj;
		this.weights = weights;
		dist = new int[adjList.length];
		processed = new boolean[adjList.length];
		
		comparator = new Comparator<Node>() {
			@Override
			public int compare(Node obj1, Node obj2){
				if(obj1.distance < obj2.distance)
					return -1;
				else if(obj1.distance > obj2.distance)
					return 1;
				else 
					return 0;
			}
		};
		INF = Integer.MAX_VALUE;
	}
	
	public void dijkstra(int origin){
		for(int i = 0; i < dist.length; ++i){
			dist[i] = INF;
			processed[i] = false;
		}
		dist[origin] = 0;
		
		queue = new PriorityQueue(dist.length, comparator);
		for(int i = 0; i < dist.length; ++i){
			if(i == origin)
				queue.add(new Node(i, 0));
			else
				queue.add(new Node(i, INF));
		}
		
		ArrayList<Integer> neighbours;
		ArrayList<Integer> neighWeights;
			
		while(!queue.isEmpty()){
			Node temp = queue.remove();
			//System.out.println("node removed = " + (temp.index + 1) + " distance = " + temp.distance
				//	+ " processed " + processed[temp.index]);
			
			if(processed[temp.index]) continue;
			else processed[temp.index] = true;
			
			neighbours = adjList[temp.index];
			neighWeights = weights[temp.index];
			for(int i = 0; i < neighbours.size(); ++i){
				relax(temp.index, neighbours.get(i), neighWeights.get(i));
			}
		}
	}
	
	public int getMinDistance(int vertex){
		return dist[vertex];
	}
	
	private boolean relax(int ini, int fin, int weight){
		if(dist[ini] == INF)
			return false;
		if(dist[fin] > dist[ini] + weight){
			dist[fin] = dist[ini] + weight;
			queue.add(new Node(fin, dist[fin]));
			return true;
		}
		return false;
	}
}