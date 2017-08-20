import java.util.*;
import java.io.*;
import java.util.zip.CheckedInputStream;

public class SuffixTree {
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

    // Build a suffix tree of the string text and return a list
    // with all of the labels of its edges (the corresponding 
    // substrings of the text) in any order.
    public List<String> computeSuffixTreeEdges(String text) {
        List<String> result = new ArrayList<String>();
        // Implement this function yourself
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
		
		for(int i = 1; i < adjList.size(); ++i){
			Node n = adjList.get(i);
			result.add(text.substring(n.startingPos, n.startingPos + n.length));
		}
		
        return result;
    }

    static public void main(String[] args) throws IOException {
        new SuffixTree().run();
    }

    public void print(List<String> x) {
        for (String a : x) {
            System.out.println(a);
        }
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        List<String> edges = computeSuffixTreeEdges(text);
        print(edges);
    }
}
