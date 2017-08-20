import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class StronglyConnected {
    private static int numberOfStronglyConnectedComponents(ArrayList<Integer>[] adj, ArrayList<Integer>[] reverseAdj) {
        //write your code here
		Graph original = new Graph(adj);
		Graph reverse = new Graph(reverseAdj);
		reverse.traverseDFS();
		int answer = 0;
		ArrayList<Integer> order = reverse.toposort();
		for(int i = order.size() - 1; i >= 0; --i){
			int v = order.get(i);
			if(!original.visited(v)){
				answer++;
				original.explore(v);
			}
		}
        return answer;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
		ArrayList<Integer>[] reverseAdj = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
			reverseAdj[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
			reverseAdj[y - 1].add(x - 1);
        }
        System.out.println(numberOfStronglyConnectedComponents(adj, reverseAdj));
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
	
	public boolean visited(int vertex){
		return visited[vertex];
	}
	
	public void explore(int vertex){
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