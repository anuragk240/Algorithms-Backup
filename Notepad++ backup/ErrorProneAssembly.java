import java.util.*;

public class ErrorProneAssembly {
  public ErrorProneAssembly() {}
  
  public static void main(String[] paramArrayOfString) { 
	Scanner scanner = new Scanner(System.in);
    int numReads = 1618;
    int readLength = 100;
    StringBuilder[] reads = new StringBuilder[numReads];
    for (int i = 0; i < numReads; i++) {
      reads[i] = new StringBuilder(scanner.nextLine());
    }
    
    Graph graph = buildGraph(reads, numReads, readLength);
    
    numReads = graph.getVertices();
	
    int totalweight = 0;
    ArrayList<Integer> pathList = new ArrayList();
    ArrayList<Integer> edgesList = new ArrayList();
    
    for (int i = 0; i < numReads; i++) {
      boolean result = graph.traverse(i, reads);
      if (result && (totalweight < graph.totalweight)) {
        totalweight = graph.totalweight;
        pathList = graph.path;
        edgesList = graph.edges;
      }
    }
    
    boolean[] corrected = new boolean[pathList.size()];
    Arrays.fill(corrected, false);
    corrected = errorCorrection(reads, readLength, pathList, edgesList, corrected);
    corrected = errorCorrection(reads, readLength, pathList, edgesList, corrected);
    
    String genome = assembleGenome(reads, pathList, edgesList);
    
    System.out.println(genome);
  }
  
  private static String assembleGenome(StringBuilder[] reads, ArrayList<Integer> path, ArrayList<Integer> edges) {
    StringBuilder genome = new StringBuilder(reads[path.get(0)]);
    
    for (int i = 1; i < path.size(); ++i) {
      int vertex = path.get(i);
      int overlap = edges.get(i - 1);
      genome.append(reads[vertex].substring(overlap));
    }
    int lastoverlap = edges.get(path.size() - 1);
    genome.delete(0, lastoverlap);
    return genome.toString();
  }
  
  private static boolean[] errorCorrection(StringBuilder[] reads, int readLength, ArrayList<Integer> path,
			ArrayList<Integer> edges, boolean[] corrected) {
    
	int numReads = path.size();
    
    for (int i = 0; i < numReads; i++) {
      int current = path.get(i);
      int next = path.get((i + 1) % numReads);
      int cn_overlap = edges.get(i);
      int mismatch = 0;
      int currentIndex = readLength - cn_overlap;
	  for (int nextIndex = 0; (currentIndex < readLength) && (mismatch < 2); currentIndex++, nextIndex++) {
        if (reads[current].charAt(currentIndex) != reads[next].charAt(nextIndex)) {
          if (corrected[current]) {
            reads[next].setCharAt(nextIndex, reads[current].charAt(currentIndex));
            corrected[next] = true;
            break; 
			}
          if (corrected[next]) {
            reads[current].setCharAt(currentIndex, reads[next].charAt(nextIndex));
            corrected[current] = true;
            break;
          }
          
          mismatch++;
          int previous = path.get((i + numReads - 1) % numReads);
          int pc_overlap = edges.get((i + numReads - 1) % numReads);
		  
          if (pc_overlap > currentIndex) {
            if (reads[current].charAt(currentIndex) == reads[previous].charAt(readLength - pc_overlap + currentIndex)) {
              reads[next].setCharAt(nextIndex, reads[current].charAt(currentIndex));
              corrected[next] = true;
              break;
			  }
            if (reads[next].charAt(nextIndex) == reads[previous].charAt(readLength - pc_overlap + currentIndex)) {
              reads[current].setCharAt(currentIndex, reads[next].charAt(nextIndex));
              corrected[current] = true;
              break;
            }
          }
          int other = path.get((i + 2) % numReads);
          int on_overlap = edges.get((i + 1) % numReads);  //overlap between other and next
          int otherIndex = 2 * numReads - on_overlap - cn_overlap;
          if (otherIndex <= currentIndex) {
            if (reads[current].charAt(currentIndex) == reads[other].charAt(currentIndex - otherIndex)) {
              reads[next].setCharAt(nextIndex, reads[current].charAt(currentIndex));
              corrected[next] = true;
			  break;
            }
            else if (reads[next].charAt(nextIndex) == reads[other].charAt(currentIndex - otherIndex)) {
              reads[current].setCharAt(currentIndex, reads[next].charAt(nextIndex));
              corrected[current] = true;
			  break;
            }
          }
        }
      }
    }
	
    return corrected;
  }
  
  private static Graph buildGraph(StringBuilder[] reads, int numReads, int readLength)
  {
    for (int i = 0; i < numReads; i++) {
      for (int j = i + 1; j < numReads; j++) {
        boolean isEqual = true;
        int mismatch = 0;
        for (int k = 0; k < readLength; k++) {
          if (reads[i].charAt(k) != reads[j].charAt(k)) { 
			mismatch++;
			if (mismatch > 2){
				isEqual = false;
				break;
			}
          }
        }
        if (isEqual) {
          reads[i] = null;
          break;
        }
      }
    }
    
    for (int i = 0; i < numReads; i++) {
      if (reads[i] == null) {
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
    
    for (int i = 0; i < numReads; i++) {
      for (int j = 0; j < numReads; j++) {
        if (i != j)
        {
          for (int k = 1; (k <= readLength - maxOverlap[i]) && (k <= readLength - 12); k++) {
            boolean match = true;
            int mismatch = 0;
            for (int l = 0; l < readLength - k; l++)
              if (reads[i].charAt(k + l) != reads[j].charAt(l)) { 
				mismatch++;
				if (mismatch > 2) {
                  match = false;
                  break;
                }
              }
            if (match) {
              maxOverlap[i] = (readLength - k);
              adjList[i].add(j);
              weights[i].add(maxOverlap[i]);
              break;
            }
          }
        }
      }
    }
    Graph graph = new Graph(adjList, weights);
    return graph;
  }
}

class Graph
{
  private ArrayList<Integer>[] adjList = null;
  private ArrayList<Integer>[] weights = null;
  private boolean[] visited;
  public int totalweight = 0;
  public ArrayList<Integer> path = null;
  public ArrayList<Integer> edges = null;
  
  public Graph(ArrayList<Integer>[] adjList, ArrayList<Integer>[] weights) {
    this.adjList = adjList;
    this.weights = weights;
    visited = new boolean[adjList.length];
    Arrays.fill(visited, false);
  }
  
  public boolean traverse(int vertex, StringBuilder[] reads) {
    int startVertex = vertex;
    
    path = new ArrayList();
    edges = new ArrayList();
    
    Arrays.fill(visited, false);
    visited[vertex] = true;
    path.add(vertex);
    int count = adjList.length - 1;
    totalweight = 0;

	while (count > 0) {
      ArrayList<Integer> neighbours = adjList[vertex];
      ArrayList<Integer> wts = weights[vertex];
      boolean deadend = true;
      for (int i = neighbours.size() - 1; i >= 0; --i) {
        if (!visited[neighbours.get(i)]) {
          int temp = wts.get(i);
          vertex = neighbours.get(i);
          visited[vertex] = true;
          path.add(vertex);
          edges.add(temp);
          totalweight += wts.get(i);
          count--;
          deadend = false;
          break;
        }
      }
      if (deadend) {
        return false;
      }
    }
    ArrayList<Integer> neighbours = adjList[vertex];
    ArrayList<Integer> wts = weights[vertex];
    boolean circular = false;
    for (int i = neighbours.size() - 1; i >= 0; --i) {
      if (neighbours.get(i) == startVertex) {
        int temp = wts.get(i);
        edges.add(temp);
        totalweight += wts.get(i);
        circular = true;
        break;
      }
    }
    if (circular) {
      return true;
    }
    return false;
  }
  
  public int getVertices()
  {
    return adjList.length;
  }
}
