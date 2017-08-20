import java.util.ArrayList;
import java.util.Scanner;

public class ConnectedComponents {
    private static int numberOfComponents(ArrayList<Integer>[] adj) {
        int result = 0;
        //write your code here
		DFS graph = new DFS(adj);
		result = graph.traverse();
        return result;
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
        System.out.println(numberOfComponents(adj));
    }
}

class DFS {
		private boolean visited[] = null;
		private ArrayList<Integer>[] adjList;
		private int connectedComp = 0;
		
		public DFS(ArrayList<Integer>[] adjList){
			this.adjList = adjList;
			visited = new boolean[adjList.length];
			for(int i = 0; i < adjList.length; ++i){
				visited[i] = false;
			}
			connectedComp = 0;
		}
		
		public int traverse(){
			for(int i = 0; i < adjList.length; ++i){
				if(!visited(i)){
					connectedComp++;
					explore(i);
				}
			}
			return connectedComp;
		}
	
		public void explore(int vertex){
			visited[vertex] = true;
			ArrayList<Integer> neighbours =  adjList[vertex];
			for(int i = 0; i < neighbours.size(); ++i){
				int v = neighbours.get(i);
				if(!visited[v])
					explore(v);
			}
		}
		
		public boolean visited(int i){
			return visited[i];
		}
}

