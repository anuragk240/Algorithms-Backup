import java.util.*;

public class ConnectingPoints {
    private static double minimumDistance(int[] x, int[] y) {
        double result = 0.;
        //write your code here
		Graph g = new Graph(x.length);
		g.makeGraph(x, y);
		result = g.primAlgo();
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] x = new int[n];
        int[] y = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = scanner.nextInt();
            y[i] = scanner.nextInt();
        }
        System.out.println(minimumDistance(x, y));
    }
}

class Graph {
	private ArrayList<Integer>[] adjList;
	private ArrayList<Double>[] weights;
	private PriorityQueue<Node> queue;
	private Comparator comparator;
	private double dist[];
	private boolean processed[];
	
	private class Node {
		int index;
		double distance;
		
		public Node(int i, double dist){
			index = i;
			distance = dist;
		}
	}
	
	public Graph(int n_vertices){
		adjList = (ArrayList<Integer>[]) new ArrayList[n_vertices];
		weights = (ArrayList<Double>[]) new ArrayList[n_vertices];
		dist = new double[n_vertices];
		processed = new boolean[n_vertices];
		
		for(int i = 0; i < n_vertices; ++i){
			adjList[i] = new ArrayList<Integer>();
			weights[i] = new ArrayList<Double>();
		}
		
		comparator = new Comparator<Node>() {
			@Override
			public int compare(Node obj1, Node obj2){
				return (int)Math.signum(obj1.distance - obj2.distance);
			}
		};
	}
	
	public double primAlgo(){
		for(int i = 1; i < adjList.length; ++i){
			dist[i] = Double.MAX_VALUE;
			processed[i] = false;
		}
		dist[0] = 0;
		
		queue = new PriorityQueue<Node>(adjList.length, comparator);
		for(int i = 0; i < adjList.length; ++i){
			queue.add(new Node(i, dist[i]));
		}
		
		double answer = 0;
		ArrayList<Integer> neighbours;
		ArrayList<Double> neighWeights;
		while(!queue.isEmpty()){
			Node temp = queue.remove();
			if(!processed[temp.index]){
				processed[temp.index] = true;
				answer += temp.distance;
			}
			else continue;
			
			neighbours = adjList[temp.index];
			neighWeights = weights[temp.index];
			for(int i = 0; i < neighbours.size(); ++i){
				update(neighbours.get(i), neighWeights.get(i));
			}
		}
		
		return answer;
	}
	
	private void update(int vertex, double weight){
		if(dist[vertex] > weight){
			dist[vertex] = weight;
			queue.add(new Node(vertex, weight));
		}
	}
	
	public void makeGraph(int[] x, int[] y){
		ArrayList<Integer> neighbours;
		ArrayList<Double> neighWeights;
		for(int i = 0; i < x.length; ++i){
			neighbours = adjList[i];
			neighWeights = weights[i];
			for(int j = 0; j < y.length; ++j){
				if(i != j){
					neighbours.add(j);
					neighWeights.add(distance(x[i], y[i], x[j], y[j]));
				}
			}
		}
	}
	
	private double distance(int x1, int y1, int x2, int y2){
		double result;
		result = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
		return Math.sqrt(result);
	}
}