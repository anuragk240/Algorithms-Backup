import java.io.*;
import java.util.*;

class Vertex {
    Vertex() {
        this.weight = 0;
        this.children = new ArrayList<Integer>();
    }

    int weight;
    ArrayList<Integer> children;
}

public class PlanParty {
    static Vertex[] ReadTree() throws IOException {
        InputStreamReader input_stream = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input_stream);
        StreamTokenizer tokenizer = new StreamTokenizer(reader);

        tokenizer.nextToken();
        int vertices_count = (int) tokenizer.nval;

        Vertex[] tree = new Vertex[vertices_count];

        for (int i = 0; i < vertices_count; ++i) {
            tree[i] = new Vertex();
            tokenizer.nextToken();
            tree[i].weight = (int) tokenizer.nval;
        }

        for (int i = 1; i < vertices_count; ++i) {
            tokenizer.nextToken();
            int from = (int) tokenizer.nval;
            tokenizer.nextToken();
            int to = (int) tokenizer.nval;
            tree[from - 1].children.add(to - 1);
            tree[to - 1].children.add(from - 1);
        }

        return tree;
    }

    static int dfs(Vertex[] tree, int vertex, int parent, int[] dmax, ArrayList<Integer> grand) {
		Vertex current = tree[vertex];
        ArrayList<Integer> children = current.children;
		
		if(children.size() == 1 && children.get(0) == parent){
			dmax[vertex] = current.weight;
			return current.weight;
		}
		
		int maxChild = 0, maxGrandChild = 0;
		ArrayList<Integer> grandChildren = new ArrayList<Integer>();
		for(int i = 0; i < children.size(); ++i){
			if(children.get(i) != parent){
				if(dmax[children.get(i)] != -1){ 
					maxChild += dmax[children.get(i)];
				}
				else{
					maxChild += dfs(tree, children.get(i), vertex, dmax, grandChildren);
				} 
			}
		}
		
		grand.add(maxChild);
		
		for(int i = 0; i < grandChildren.size(); ++i){
			maxGrandChild += grandChildren.get(i);
		}
		maxGrandChild += current.weight;
		
		if(maxChild > maxGrandChild){
			dmax[vertex] = maxChild;
			return maxChild;
		}else{
			dmax[vertex] = maxGrandChild;
			return maxGrandChild;
		}
		
    }

    static int MaxWeightIndependentTreeSubset(Vertex[] tree) {
        int size = tree.length;
        if (size == 0)
            return 0;
		int[] dmax = new int[tree.length];
		Arrays.fill(dmax, -1);
        int max = dfs(tree, 0, -1, dmax, new ArrayList<Integer>());
		// for(int i = 0; i < dmax.length; ++i){
			// System.out.print(dmax[i] + " ");
		// }
		// System.out.println();
		return max;
    }

    public static void main(String[] args) throws IOException {
      // This is to avoid stack overflow issues
      new Thread(null, new Runnable() {
                    public void run() {
                        try {
                            new PlanParty().run();
                        } catch(IOException e) {
                        }
                    }
                }, "1", 1 << 26).start();
    }

    public void run() throws IOException {
        Vertex[] tree = ReadTree();
        int weight = MaxWeightIndependentTreeSubset(tree);
        System.out.println(weight);
    }
}
