import java.util.ArrayList;
import java.util.Arrays;












































































































































































class Graph
{
  private ArrayList<Integer>[] adjList = null;
  private ArrayList<Integer>[] weights = null;
  private boolean[] visited;
  public int totalweight = 0;
  public ArrayList<Integer> path = null;
  public ArrayList<Integer> edges = null;
  
  public Graph(ArrayList<Integer>[] paramArrayOfArrayList1, ArrayList<Integer>[] paramArrayOfArrayList2) {
    adjList = paramArrayOfArrayList1;
    weights = paramArrayOfArrayList2;
    visited = new boolean[paramArrayOfArrayList1.length];
    Arrays.fill(visited, false);
  }
  
  public boolean traverse(int paramInt, StringBuilder[] paramArrayOfStringBuilder) {
    int i = paramInt;
    
    path = new ArrayList();
    edges = new ArrayList();
    
    Arrays.fill(visited, false);
    visited[paramInt] = true;
    path.add(Integer.valueOf(paramInt));
    int j = adjList.length - 1;
    totalweight = 0;
    int n; while (j > 0) {
      localArrayList1 = adjList[paramInt];
      localArrayList2 = weights[paramInt];
      k = 1;
      for (m = localArrayList1.size() - 1; m >= 0; m--) {
        if (visited[((Integer)localArrayList1.get(m)).intValue()] == 0) {
          n = ((Integer)localArrayList2.get(m)).intValue();
          paramInt = ((Integer)localArrayList1.get(m)).intValue();
          visited[paramInt] = true;
          path.add(Integer.valueOf(paramInt));
          edges.add(Integer.valueOf(n));
          totalweight += ((Integer)localArrayList2.get(m)).intValue();
          j--;
          k = 0;
          break;
        }
      }
      if (k != 0) {
        return false;
      }
    }
    ArrayList localArrayList1 = adjList[paramInt];
    ArrayList localArrayList2 = weights[paramInt];
    int k = 0;
    for (int m = localArrayList1.size() - 1; m >= 0; m--) {
      if (((Integer)localArrayList1.get(m)).intValue() == i) {
        n = ((Integer)localArrayList2.get(m)).intValue();
        edges.add(Integer.valueOf(n));
        totalweight += ((Integer)localArrayList2.get(m)).intValue();
        k = 1;
        break;
      }
    }
    if (k != 0) {
      return true;
    }
    return false;
  }
  
  public int getVertices()
  {
    return adjList.length;
  }
}
