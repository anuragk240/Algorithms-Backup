import java.util.ArrayList;
import java.util.Scanner;

public class Acyclicity {
    private static int acyclic(ArrayList<Integer>[] adj) {
        //write your code here
		Graph g = new Graph(adj);
		boolean ans = g.traverseDFS();
		if(ans) return 1;
		else return 0;
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
        System.out.println(acyclic(adj));
    }
}

class Graph {
	private ArrayList<Integer>[] adjList = null;
	private boolean visited[];
	private boolean deleted[];
	private boolean isCyclic;
	
	public Graph(ArrayList<Integer>[] adjList){
		this.adjList = adjList;
		visited = new boolean[adjList.length];
		deleted = new boolean[adjList.length];
		for(int i = 0; i < adjList.length; ++i){
			visited[i] = false;
			deleted[i] = false;
		}
		isCyclic = false;
	}
	
	public boolean traverseDFS(){
		isCyclic = false;
		for(int i = 0; (i < adjList.length) && (!isCyclic); ++i){
			if(!deleted(i)){
				if(!visited(i)){
					explore(i);
				}else {
					isCyclic = true;
					//System.out.println("DFSvisited node = " + i);
					break;
				}
			}
		}
		return isCyclic;
	}
	
	public boolean visited(int vertex){
		return visited[vertex];
	}
	
	public boolean deleted(int vertex){
		return deleted[vertex];
	}
	
	private void explore(int vertex){
		visited[vertex] = true;
		ArrayList<Integer> neighbours = adjList[vertex];
		
		for(int i = 0; i < neighbours.size(); ++i){
			int v = neighbours.get(i);
			if(!deleted(v)){
				if(!visited(v)){
					explore(v);
				}else {
					isCyclic = true;
					//System.out.println("visited node = " + i);
					break;
				}
			}
		}
		deleted[vertex] = true;
	}
}