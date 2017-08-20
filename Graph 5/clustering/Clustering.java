import java.util.*;

public class Clustering {
    private static double clustering(int[] x, int[] y, int k) {
        //write your code here
		Point[] points = new Point[x.length];
		for(int i = 0; i < x.length; ++i){
			points[i] = new Point(x[i], y[i]);
		}
		
		Comparator<Edge> comparator = new Comparator<Edge>(){
			@Override
				public int compare(Edge obj1, Edge obj2){
					return (int)Math.signum(obj1.getWeight() - obj2.getWeight());
				}
		};
		PriorityQueue<Edge> queue = new PriorityQueue<>(x.length * (x.length - 1) / 2, comparator);
		
		for(int i = 0; i < x.length; ++i){
			for(int j = i + 1; j < y.length; ++j){
				queue.add(new Edge(i, j, points));
			}
		}
		
		DisjointSet ds = new DisjointSet(points.length);
		Edge temp = null;
		double result = 0;
		while(ds.getnumberOfSets() != k - 1){
			temp = queue.remove();
			ds.union(temp.getu(), temp.getv());
			result = temp.getWeight();
		}
		
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
        int k = scanner.nextInt();
        System.out.println(clustering(x, y, k));
    }
}

class DisjointSet {
	private int[] rank;
	private int nSets;
	private int[] parent;
	
	public DisjointSet(int num){
		nSets = num;
		rank = new int[nSets];
		parent = new int[nSets];
		for(int i = 0; i < nSets; ++i){
			rank[i] = 1;
			parent[i] = i;
		}
	}
	
	public int find(int p){
		while(parent[p] != p){
			p = parent[p];
		}
		return p;
	}
	
	public void union(int p1, int p2){
		int rank1, rank2, root1, root2;
		root1 = find(p1);
		root2 = find(p2);
		rank1 = rank[root1];
		rank2 = rank[root2];
		
		if(root1 == root2) return;
		
		if(rank1 > rank2){
			parent[root2] = root1;
		}else if(rank2 > rank1){
			parent[root1] = root2;
		}else{
			parent[root2] = root1;
			++rank[root1];
		}
		--nSets;
	}
	
	public int getnumberOfSets(){
		return nSets;
	}
}

class Point{
	int x;
	int y;

	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
}

class Edge{
	private int u;
	private int v;
	private double weight;
	
	public Edge(int u, int v, Point[] points){
		this.u = u;
		this.v = v;
		weight = distance(points[u], points[v]);
	}
	
	private double distance(Point a, Point b){
		int x1 = a.x, y1 = a.y, x2 = b.x, y2 = b.y;
		double result;
		result = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
		return Math.sqrt(result);
	}
	
	public double getWeight(){
		return weight;
	}
	
	public int getu(){
		return u;
	}
	
	public int getv(){
		return v;
	}
}