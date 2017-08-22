import java.util.*;

public class UniversalCircularString{
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		int k = scanner.nextInt();
		int iterations = (int)Math.pow(2, k);
		int iteration_minus1 = (int)Math.pow(2, k - 1);
		
		String[] k_mers = new String[iterations];
		String[] k_minus1_mers = new String[iteration_minus1];
		
		for(int i = 0; i < iteration_minus1; ++i){
			StringBuilder builder = new StringBuilder(Integer.toBinaryString(i));
			k_mers[i] = appendZeroes(builder, k).toString();
			k_minus1_mers[i] = builder.delete(0, 1).toString();
			//System.out.println(k_mers[i]);
			//System.out.println(k_minus1_mers[i]);
		}
		
		for(int i = iteration_minus1; i < iterations; ++i){
			StringBuilder builder = new StringBuilder(Integer.toBinaryString(i));
			k_mers[i] = appendZeroes(builder, k).toString();
			//System.out.println(k_mers[i]);
		}
		
		Graph graph = buildGraph(iteration_minus1, iterations, k_minus1_mers, k_mers);
		
		graph.buildCycle(0);
		DoublyLinkedList cycle = graph.getEulerianCycle();
		//cycle is in reversed order
		StringBuilder universalString = new StringBuilder();
		Node node = cycle.start;
		while(node.next != null){
			node = node.next;
		}
		//traverse in reverse order from here
		universalString.append(k_minus1_mers[node.vertex]);
		node = node.previous;
		while(node != null){
			//System.out.print((node.vertex + 1) + " ");
			universalString.append(k_minus1_mers[node.vertex].charAt(k - 2));  //k is the length of k-1 mer
			node = node.previous;
		}
		//System.out.println();
		universalString.delete(0, k - 2);
		System.out.println(universalString.toString());
	}
	
	public static Graph buildGraph(int numVertex, int numEdges, String[] vertices, String[] edges){
		DoublyLinkedList[] adjList = new DoublyLinkedList[numVertex];
		for(int i = 0; i < numVertex; ++i){
			adjList[i] = new DoublyLinkedList();
		}
		for(int i = 0; i < numEdges; ++i){
			String prefix = edges[i].substring(0, edges[i].length() - 1);
			String suffix = edges[i].substring(1, edges[i].length());
			adjList[Integer.parseInt(prefix, 2)].insert(new Node(Integer.parseInt(suffix, 2)));
		}
		
		Graph graph = new Graph(adjList);
		return graph;
	}
	
	private static StringBuilder appendZeroes(StringBuilder string, int k){
		string = string.reverse();
		for(int i = string.length(); i < k; ++i){
			string.append('0');
		}
		return string.reverse();
	}
}

class Graph {
	private DoublyLinkedList[] adjList = null;
	private DoublyLinkedList cycle;

	public Graph(DoublyLinkedList[] adjList){
		this.adjList = adjList;
		cycle = new DoublyLinkedList();
	}
	
	public DoublyLinkedList getEulerianCycle(){
		return cycle;
	}
	
	public void buildCycle(int startVertex){
		cycle = traverse(startVertex);
		
		Node position = cycle.start;
		DoublyLinkedList neighbours;
		while(position != null){
			neighbours = adjList[position.vertex];
			if(!neighbours.isEmpty()){
				mergeCycles(traverse(position.vertex), position.previous);
			}
			position = position.next;
		}
		
	}
	
	private DoublyLinkedList traverse(int origin){
		DoublyLinkedList neighbours = adjList[origin];
		DoublyLinkedList temp = new DoublyLinkedList();
		while(!neighbours.isEmpty()){
			Node n = neighbours.delete();
			temp.insert(n);
			neighbours = adjList[n.vertex];
		}
		return temp;
	}
	
	private void mergeCycles(DoublyLinkedList tempCycle, Node pos){
		Node n = pos.next;
		Node temp = n;
		n = tempCycle.start;
		n.previous = pos;
		while(n.next != null){
			n = n.next;
		}
		n.next = temp;
		temp.previous = n;
	}

}


class Node {
	public int vertex;
	public Node next;
	public Node previous;
	
	public Node(int vertex){
		this.vertex = vertex;
		next = null;
		previous = null;
	}
}

class DoublyLinkedList {
	public Node start;
	
	public DoublyLinkedList(){
		start = null;
	}
	
	public Node delete(){
		if(start == null){
			return null;
		}else if(start.next == null){
			Node temp = start;
			start = null;
			temp.previous = null;
			return temp;
		}else{
			Node temp = start;
			start = start.next;
			start.previous = null;
			temp.next = null;
			return temp;
		}
	}
	
	public boolean isEmpty(){
		return start == null;
	}
	
	public void insert(Node data){
		if(start == null){
			start = data;
		}else {
			data.next = start;
			start.previous = data;
			start = data;
			data.previous = null;
		}
	}
	
}
