import java.util.*;

public class EulerCycle{
	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        DoublyLinkedList[] adj = new DoublyLinkedList[n];

        for (int i = 0; i < n; i++) {
            adj[i] = new DoublyLinkedList();
        }
		int[] indegree = new int[n];
		int[] outdegree = new int[n];
		Arrays.fill(indegree, 0);
		Arrays.fill(outdegree, 0);
		
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].insert(new Node(y - 1));
			indegree[y - 1]++;
			outdegree[x - 1]++;
        }
		
		boolean isEuler = true;
		for(int i = 0; i < n; ++i){
			if(indegree[i] != outdegree[i]){
				isEuler = false;
				break;
			}
		}
		if(isEuler){
			Graph graph = new Graph(adj);
			graph.buildCycle(0);
			DoublyLinkedList result = graph.getEulerianCycle();
			System.out.println(1);
			Node node = result.start;
			while(node.next != null){
				node = node.next;
			}
			while(node != null){
				System.out.print((node.vertex + 1) + " ");
				node = node.previous;
			}
			System.out.println();
		}else{
			System.out.println(0);
		}
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



