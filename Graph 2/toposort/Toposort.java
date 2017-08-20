import java.util.*;

public class Toposort {
    private static ArrayList<Integer> toposort(ArrayList<Integer>[] adj) {
        ArrayList<Integer> order = new ArrayList<Integer>();
        //write your code here
		Graph g = new Graph(adj);
		g.traverseDFS();
		order = g.toposort();
        return order;
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
        }
        ArrayList<Integer> order = toposort(adj);
        for (int x = order.size() - 1; x >= 0; --x) {
            System.out.print((order.get(x) + 1) + " ");
        }
    }
}

class Graph {
	private ArrayList<Integer>[] adjList = null;
	private boolean visited[];
	ArrayList<Integer> result;

	public Graph(ArrayList<Integer>[] adjList){
		this.adjList = adjList;
		visited = new boolean[adjList.length];

		for(int i = 0; i < adjList.length; ++i){
			visited[i] = false;
		}
		result = new ArrayList<Integer>(adjList.length);
	}

	public void traverseDFS(){
		for(int i = 0; i < adjList.length; ++i){
			if(!visited(i)){
				explore(i);
				postvisit(i);
			}
		}
	}

	public ArrayList<Integer> toposort(){
		return result;
	}

	private void postvisit(int vertex){
		result.add(vertex);
	}

	private boolean visited(int vertex){
		return visited[vertex];
	}

	private void explore(int vertex){
		visited[vertex] = true;
		ArrayList<Integer> neighbours = adjList[vertex];

		for(int i = 0; i < neighbours.size(); ++i){
			int v = neighbours.get(i);
			if(!visited(v)){
				explore(v);
				postvisit(v);
			}
		}
	}
}
