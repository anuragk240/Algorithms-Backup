import java.io.*;
import java.util.*;

class RopeProblem {
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

	class Rope {
		String s;

		void process( int i, int j, int k ) {
			// Replace this code with a faster implementation
			VertexPair leftMedium = split(i, root);
			VertexPair MediumRight = split(j + 1 - i, leftMedium.right);
			Vertex left = leftMedium.left;
			Vertex medium = MediumRight.left;
			Vertex right = MediumRight.right;
			Vertex node = merge(left, right);
			VertexPair result = split(k, node);
			
			root = merge(merge(result.left, medium), result.right);
		}

		String result() {
			StringBuilder str = new StringBuilder();
			inOrderTraversal(root, str);
			return str.toString();
		}
		Rope( String s ) {
			this.s = s;
		}
	}
	
	Vertex root;
	
	class Vertex {
		int size;
		char alphabet;
		Vertex parent;
		Vertex leftChild;
		Vertex rightChild;
		
		Vertex(char alpha, Vertex parent, Vertex left, Vertex right){
			alphabet = alpha;
			this.parent = parent;
			leftChild = left;
			rightChild = right;
			size = 1 + ((leftChild != null)? leftChild.size : 0) + ((rightChild != null)? rightChild.size : 0);
		}
	}
	
	class VertexPair {
		Vertex left;
		Vertex right;
		
		VertexPair(Vertex left, Vertex right){
			this.left = left;
			this.right = right;
		}
	}
	
	private void inOrderTraversal(Vertex root, StringBuilder str){
		if(root == null) return;
		inOrderTraversal(root.leftChild, str);
		str.append(root.alphabet);
		inOrderTraversal(root.rightChild, str);
	}
	
	private VertexPair split(int index, Vertex root){
		Vertex v = find(index + 1, root);
		VertexPair result = new VertexPair(null, null);
		
		if(v == null){
			result.left = root;
			result.right = v;
			return result;
		}
		
		root = splay(v);
		result.left = root.leftChild;
		root.leftChild = null;
		result.right = root;
		if(result.left != null)
			result.left.parent = null;
		
		updateVertex(result.right);
		updateVertex(result.left);
		return result;
	}
	
	private Vertex merge(Vertex left, Vertex right){
		if(left == null) return right;
		if(right == null) return left;
		
		Vertex result;
		Vertex v = find(left.size, left);
		result = splay(v);
		result.rightChild = right;
		
		updateVertex(result);
		
		return result;
	}
	
	private Vertex getNode(int left, int right, String str){
		int mid = (left + right) / 2;
		
		if (left > right) return null;
		if (left == right){
			return new Vertex(str.charAt(left), null, null, null);
		}
		
		Vertex lchild, rchild;
		lchild = getNode(left, mid - 1, str);
		rchild = getNode(mid + 1, right, str);
		
		Vertex v = new Vertex(str.charAt(mid), null, lchild, rchild);
		updateVertex(v);
		return v;
	}
	
	private void updateVertex(Vertex v){
		if(v == null) return;
		v.size = 1 + ((v.leftChild != null)? v.leftChild.size : 0) + ((v.rightChild != null)? v.rightChild.size : 0);
		if(v.leftChild != null){
			v.leftChild.parent = v;
		}
		if(v.rightChild != null){
			v.rightChild.parent = v;
		}
	}
	
	private Vertex find(int index, Vertex v){
		if(v == null)
			return v;
		
		int nleftNodes = (v.leftChild == null)? 0 : v.leftChild.size;
		if(index == nleftNodes + 1)
			return v;
		else if(index < nleftNodes + 1)
			return find(index, v.leftChild);
		else
			return find(index - nleftNodes - 1, v.rightChild);
	}
	
	private Vertex splay(Vertex node){
		if(node == null) return null;
		
		while(node.parent != null){
			doubleRotation(node);
		}
		return node;
	}
	
	private void smallRotation(Vertex node){
		Vertex parent = node.parent;
		Vertex temp, grandParent = node.parent.parent;
		if(parent.leftChild == node){
			//rotate right
			temp = node.rightChild;
			node.rightChild = parent;
			parent.leftChild = temp;
		}else{
			//rotate left
			temp = node.leftChild;
			node.leftChild = parent;
			parent.rightChild = temp;
		}
		updateVertex(node);
		updateVertex(parent);
		node.parent = grandParent;
		if(grandParent != null){
			if(grandParent.leftChild == parent){
				grandParent.leftChild = node;
			}else{
				grandParent.rightChild = node;
			}
		}
	}
	
	private void doubleRotation(Vertex v){
		Vertex grandParent = v.parent.parent;
		Vertex parent = v.parent;
		
		//zig case
		if(grandParent == null){
			smallRotation(v);
			return;
		}
		
		//zig-zig case
		if(grandParent.leftChild == parent && parent.leftChild == v){
			smallRotation(parent);
			smallRotation(v);
		}else if(grandParent.rightChild == parent && parent.rightChild == v){
			smallRotation(parent);
			smallRotation(v);
		}else {
			//zig-zag case
			smallRotation(v);
			smallRotation(v);
		}
		
	}

	public static void main( String[] args ) throws IOException {
		new RopeProblem().run();
	}
	public void run() throws IOException {
		FastScanner in = new FastScanner();
		PrintWriter out = new PrintWriter(System.out);
		Rope rope = new Rope(in.next());
		
		root = getNode(0, rope.s.length() - 1, rope.s);
		//StringBuilder str = new StringBuilder();
		//inOrderTraversal(root, str);
		//System.out.println("input string = " + str.toString());
		
		for (int q = in.nextInt(); q > 0; q--) {
			int i = in.nextInt();
			int j = in.nextInt();
			int k = in.nextInt();
			rope.process(i, j, k);
		}
		out.println(rope.result());
		out.close();
	}
}
