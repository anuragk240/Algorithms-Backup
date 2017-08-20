import java.io.*;
import java.math.*;
import java.util.*;

public class NonSharedSubstring implements Runnable {
	String solve (String text1, String text2) {
		String result = null;
		//Code
		List<Node> adjList = buildSuffixTree(text2);
		
		int minLength = text1.length() + 1;
		
		for(int i = 0; i < text1.length(); ++i){
			Node currentNode = adjList.get(0);
			innerLoop:
			for(int k = i; k < text1.length() && minLength > k - i + 1;){
				char c = text1.charAt(k);
				int next = currentNode.next[letterToIndex(c)];
				boolean matchFound = false;
				Node nextNode = null;
				
				if(next != Node.NA){
					nextNode = adjList.get(next);
					matchFound = true;
					++k;
				}
				
				if(matchFound){
					for(int j = nextNode.startingPos + 1; j < nextNode.length + nextNode.startingPos; ++j, ++k){
						if(minLength <= k - i + 1 || k >= text1.length())
							break innerLoop;
						
						if(text2.charAt(j) != text1.charAt(k)){
							minLength = k - i + 1;
							result = text1.substring(i, k + 1);
							break innerLoop;
						}
					}
					currentNode = nextNode;
				}else if(minLength > k - i + 1){
					minLength = k - i + 1;
					result = text1.substring(i, k + 1);
					break;
				}else break;
				
			}
		}
		
		return result;
	}
	
	class Node{
		public static final int Letters =  5;
		public static final int NA      = -1;
		public int next [];
		public int startingPos;
		public int length;

		Node ()
		{
			next = new int [Letters];
			Arrays.fill (next, NA);
			startingPos = -1;
			length = -1;
		}
	}
	
	int letterToIndex (char letter){
		switch (letter)
		{
			case 'A': return 0;
			case 'C': return 1;
			case 'G': return 2;
			case 'T': return 3;
			case '$': return 4;
			default: assert (false); return Node.NA;
		}
	}
	
	public List<Node> buildSuffixTree(String text) {
		ArrayList<Node> adjList = new ArrayList<Node>();
		
		//Added root Node with no edges
		adjList.add(new Node());
		int totalNodes = 0;
		
		for(int i = 0; i < text.length(); ++i){
			//Set edge list to the edges of root node
			Node currentNode = adjList.get(0);
			
			innerLoop:
			for(int k = i; k < text.length();){
				boolean matchFound = false;
				char firstchar = text.charAt(k);
				int next = currentNode.next[letterToIndex(firstchar)];
				Node nextNode = null;
				
				if(next != Node.NA){
					nextNode = adjList.get(next);
					matchFound = true;
					++k;
				}
				
				//System.out.println("Match Found : " + matchFound + " i=" + i);
			
				if(matchFound){
					for(int j = nextNode.startingPos + 1; j < nextNode.length + nextNode.startingPos; ++j, ++k){
						if(k >= text.length())
							break innerLoop;
						
						if(text.charAt(j) != text.charAt(k)){
							//Branching
							Node first = new Node();
							first.startingPos = nextNode.startingPos;
							first.length = j - nextNode.startingPos;
							first.next[letterToIndex(text.charAt(j))] = next;
							first.next[letterToIndex(text.charAt(k))] = totalNodes + 2;
							
							Node second = new Node();
							second.startingPos = k;
							second.length = text.length() - k;
							
							currentNode.next[letterToIndex(firstchar)] = totalNodes + 1;
							
							nextNode.startingPos = j;
							nextNode.length -= first.length;
							
							adjList.add(first);
							adjList.add(second);
							
							totalNodes +=2;
							
							break innerLoop;
						}
					}
					currentNode = nextNode;
				}else {
					Node node = new Node();
					totalNodes++;
					node.startingPos = k;
					node.length = text.length() - k;
					currentNode.next[letterToIndex(firstchar)] = totalNodes;
					adjList.add(node);
					break;
				}
			}
			
		}
		
        return adjList;
    }

	public void run () {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
			String p = in.readLine ();
			String q = in.readLine ();

			String ans = solve (p, q);

			System.out.println (ans);
		}
		catch (Throwable e) {
			e.printStackTrace ();
			System.exit (1);
		}
	}

	public static void main (String [] args) {
		new Thread (new NonSharedSubstring ()).start ();
	}
}
