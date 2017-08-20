import java.util.*;

public class ErrorFreeAssembly{
	
	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numReads = 1618;
		int readlength = 100;
		StringBuilder[] reads = new StringBuilder[numReads]; 
		for(int i = 0 ; i < numReads; ++i){
			reads[i] = new StringBuilder(scanner.nextLine());
		}
		
		Graph graph = buildGraph(reads, numReads, readlength);
		
		numReads = graph.getVertices();
		boolean result;
		int totalweight = 0;
		String genome = null;
		for(int i = 0; i < numReads; ++i){
			result = graph.traverse(i, reads);
			if(result && totalweight < graph.totalweight){
				totalweight = graph.totalweight;
				genome = graph.genome.toString();
			}
		}
		
		System.out.println(genome);
    }
	
	
	
	private static Graph buildGraph(StringBuilder[] reads, int numReads, int readlength){
		//eleminate redundant reads
		for(int i = 0; i < numReads; ++i){
			for(int j = i + 1; j < numReads; ++j){
				boolean isequal = true;
				for(int k = 0; k < readlength; ++k){
					if(reads[i].charAt(k) != reads[j].charAt(k)){
						isequal = false;
						break;
					}
				}
				if(isequal){
					reads[i] = null;
					break;
				}
			}
		}
		
		for(int i = 0; i < numReads; ++i){
			if(reads[i] == null){
				reads[i] = reads[--numReads];
				--i;
			}
		}
		
		ArrayList<Integer>[] adjList = new ArrayList[numReads];
		ArrayList<Integer>[] weights = new ArrayList[numReads];
		for (int i = 0; i < numReads; i++) {
            adjList[i] = new ArrayList<Integer>();
            weights[i] = new ArrayList<Integer>();
        }
		
		int[] maxOverlap = new int[numReads];
		Arrays.fill(maxOverlap, 0);
		
		for(int i = 0 ; i < numReads; ++i){
			for(int j = 0; j < numReads; ++j){
				if(i == j) continue;
				//int temp = readlength - maxOverlap[i];
				for(int k = 1; k <= readlength - maxOverlap[i]; ++k){
					boolean match = true;
					for(int l = 0; l < readlength - k; ++l){
						if(reads[i].charAt(k + l) != reads[j].charAt(l)){
							match = false;
							break;
						}
					}
					if(match){
						maxOverlap[i] = readlength - k;
						adjList[i].add(j);
						weights[i].add(maxOverlap[i]);
						break;
					}
				}
			}
		}
		
		Graph graph = new Graph(adjList, weights);
		return graph;
	}
	
}

class Graph {
	private ArrayList<Integer>[] adjList = null;
	private ArrayList<Integer>[] weights = null;
	private boolean visited[];
	public StringBuilder genome = null;
	public int totalweight = 0;
	
	public Graph(ArrayList<Integer>[] adjList, ArrayList<Integer>[] weights){
		this.adjList = adjList;
		this.weights = weights;
		visited = new boolean[adjList.length];
		Arrays.fill(visited, false);
	}
	
	public boolean traverse(int vertex, StringBuilder[] reads){
		int startvertex = vertex;
		Arrays.fill(visited, false);
		genome = new StringBuilder(reads[vertex].toString());
		visited[vertex] = true;
		int count = adjList.length - 1;
		totalweight = 0;
		while(count > 0){
			ArrayList<Integer> neighbours = adjList[vertex];
			ArrayList<Integer> wts = weights[vertex];
			boolean deadend = true;
			for(int i = neighbours.size() - 1; i >= 0; --i){
				if(!visited[neighbours.get(i)]){
					int temp = wts.get(i);
					vertex = neighbours.get(i);
					genome.append(reads[vertex].substring(temp));
					visited[vertex] = true;
					totalweight += wts.get(i);
					count--;
					deadend = false;
					break;
				}
			}
			if(deadend){
				return false;
			}
		}
		ArrayList<Integer> neighbours = adjList[vertex];
		ArrayList<Integer> wts = weights[vertex];
		boolean circular = false;
		for(int i = neighbours.size() - 1; i >= 0; --i){
			if(neighbours.get(i) == startvertex){
				int temp = wts.get(i);
				genome.delete(0, temp);
				totalweight += wts.get(i);
				circular = true;
				break;
			}
		}
	if(circular)
		return true;
	else
		return false;
		
	}
	
	public int getVertices(){
		return adjList.length;
	}
}