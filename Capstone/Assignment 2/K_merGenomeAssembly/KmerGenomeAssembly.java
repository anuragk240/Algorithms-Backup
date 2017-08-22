import java.util.*;

public class KmerGenomeAssembly {
	public static void main(String[] args){
		int lines = 5396;
		int k = 10;
		Scanner scanner = new Scanner(System.in);
		String[] reads = new String[lines];
		HashMap<String, Integer> verticesMapping = new HashMap();
		ArrayList<String> vertices = new ArrayList();
		int numVertices = 0;
		for(int i = 0; i < lines; ++i){
			reads[i] = scanner.nextLine();
			String prefix = reads[i].substring(0, reads[i].length() - 1);
			String suffix = reads[i].substring(1, reads[i].length());
			if(verticesMapping.get(prefix) == null){
				vertices.add(prefix);
				verticesMapping.put(prefix, numVertices);
				numVertices++;
			}
			
			if(verticesMapping.get(suffix) == null){
				vertices.add(suffix);
				verticesMapping.put(suffix, numVertices);
				numVertices++;
			}
		}

		Graph graph = buildGraph(numVertices, lines, verticesMapping, reads);
		graph.buildCycle(0);
		DoublyLinkedList cycle = graph.getEulerianCycle();
		
		//cycle is in reversed order
		StringBuilder genome = new StringBuilder();
		Node node = cycle.start;
		while(node.next != null){
			node = node.next;
		}
		//traverse in reverse order from here
		genome.append(vertices.get(node.vertex));
		node = node.previous;
		while(node != null){
			genome.append(vertices.get(node.vertex).charAt(k - 2));  //k is the length of k-mer
			node = node.previous;
		}
		//System.out.println();
		genome.delete(0, k - 2);
		System.out.println(genome.toString());
	}
	
	public static Graph buildGraph(int numVertex, int numEdges, HashMap<String, Integer> vertices, String[] edges){
		DoublyLinkedList[] adjList = new DoublyLinkedList[numVertex];
		for(int i = 0; i < numVertex; ++i){
			adjList[i] = new DoublyLinkedList();
		}
		for(int i = 0; i < numEdges; ++i){
			String prefix = edges[i].substring(0, edges[i].length() - 1);
			String suffix = edges[i].substring(1, edges[i].length());
			adjList[vertices.get(prefix)].insert(new Node(vertices.get(suffix)));
		}
		
		Graph graph = new Graph(adjList);
		return graph;
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
