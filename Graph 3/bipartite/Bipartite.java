import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Bipartite {
    private static int bipartite(ArrayList<Integer>[] adj) {
        //write your code here
		Graph g = new Graph(adj);
		g.traversBFS(0);
		if(g.isBipartite())
			return 1;
		else
			return 0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
            adj[y - 1].add(x - 1);
        }
        System.out.println(bipartite(adj));
    }
}

class Graph {
	private ArrayList<Integer>[] adjList;
	private int[] distance;
	private ArrayList<ArrayList<Integer>> layers;
	private Queue<Integer> queue = null;
	
	public Graph(ArrayList<Integer>[] adj){
		adjList = adj;
		distance = new int[adjList.length];
		layers = new ArrayList<ArrayList<Integer>>();
	}
	
	public void traversBFS(int origin){
		for(int i = 0; i < adjList.length; ++i){
			distance[i] = -1;
		}
		queue = new LinkedList<Integer>();
		queue.offer(origin);
		distance[origin] = 0;
		if(distance[origin] >= layers.size()){
			layers.add(new ArrayList<Integer>());
		}
		layers.get(distance[origin]).add(origin);
		ArrayList<Integer> neighbours;
		while(queue.peek() != null){
			int vertex = queue.poll();
			neighbours = adjList[vertex];
			int v;
			for(int i = 0; i < neighbours.size(); ++i){
				v = neighbours.get(i);
				if(distance[v] == -1){
					queue.offer(v);
					distance[v] = distance[vertex] + 1;
					if(distance[v] >= layers.size()){
						layers.add(new ArrayList<Integer>());
					}
					layers.get(distance[v]).add(v);
				}
			}
		}
	}
	
	public boolean isBipartite(){
		ArrayList<Integer> l = null;
		for(int i = 1; i < layers.size(); ++i){
			l = layers.get(i);
			for(int j = 0; j < l.size() - 1; ++j){
				int vertex = l.get(j);
				ArrayList<Integer> neighbours = adjList[vertex];
				for(int k = 0; k < neighbours.size(); ++k){
					int v = neighbours.get(k);
					if(distance[v] == distance[vertex])
						return false;
				}
			}
		}
		return true;
	}
}