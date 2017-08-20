import java.util.*;
import java.io.*;

public class tree_height {
    class FastScanner {
		StringTokenizer tok = new StringTokenizer("");
		BufferedReader in;

		FastScanner() {
			in = new BufferedReader(new InputStreamReader(System.in));
		}

		String next() throws IOException {
			while (!tok.hasMoreElements())
				tok = new StringTokenizer(in.readLine());
			return tok.nextToken();
		}
		int nextInt() throws IOException {
			return Integer.parseInt(next());
		}
	}
	
	public class Node{
		int num;
		ArrayList<Node> child = null;
		
		public Node(int num){
			this.num = num;
		}
		
		public void addChild(Node child){
			if(this.child == null){
				this.child = new ArrayList<Node>();
			}
			this.child.add(child);
		}
		
		public ArrayList<Node> getChild(){
			return child;
		}
	}

	public class TreeHeight {
		int n;
		int parent[];
		Node root;
		
		void read() throws IOException {
			FastScanner in = new FastScanner();
			n = in.nextInt();
			parent = new int[n];
			for (int i = 0; i < n; i++) {
				parent[i] = in.nextInt();
			}
		}
		
		public void buildTree(){
			Node nodes[] = new Node[n];
			for(int i = 0; i < n; ++i){
				nodes[i] = new Node(i);
				if(parent[i] == -1){
					root = nodes[i];
				}
			}
			
			for(int i = 0; i < n; ++i){
				if(parent[i] != -1){
					nodes[parent[i]].addChild(nodes[i]);
				}
			}
		}

		int computeHeight(Node i) {
            // Replace this code with a faster implementation
			ArrayList<Node> nodes = i.getChild();
			if(nodes == null){
				return 1;
			} else{
				int maxHeight = 0;
				for (int vertex = 0; vertex < nodes.size(); vertex++) {
					int temp = computeHeight(nodes.get(vertex));
					if(maxHeight < temp){
						maxHeight = temp;
					}
				}
				return 1 + maxHeight;
			}
		}
	}

	static public void main(String[] args) throws IOException {
            new Thread(null, new Runnable() {
                    public void run() {
                        try {
                            new tree_height().run();
                        } catch (IOException e) {
                        }
                    }
                }, "1", 1 << 26).start();
	}
	public void run() throws IOException {
		TreeHeight tree = new TreeHeight();
		tree.read();
		tree.buildTree();
		System.out.println(tree.computeHeight(tree.root));
	}
}
