import java.io.*;
import java.util.*;

class Node
{
	public static final int Letters =  5;
	public static final int NA      = -1;
	public int next [];
	public boolean isLeave;

	Node ()
	{
		next = new int [Letters];
		Arrays.fill (next, NA);
		isLeave = true;
	}
}

public class TrieMatchingExtended implements Runnable {
	int letterToIndex (char letter)
	{
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
	
	List<Node> buildTrie(List<String> patterns) {
        List<Node> trie = new ArrayList<Node>();

        // write your code here
		int totalnodes = 0;
		trie.add(new Node());
		
		for(int i = 0; i < patterns.size(); ++i){
			String pattern = patterns.get(i);
			char letter;
			int node = 0;
			for(int j = 0; j < pattern.length(); ++j){
				letter = pattern.charAt(j);
				Node edges = trie.get(node);
				if(edges.next[letterToIndex(letter)] == Node.NA){
					edges.next[letterToIndex(letter)] = totalnodes + 1;
					totalnodes++;
					edges.isLeave = false;
					trie.add(new Node());
					node = totalnodes;
				}else {
					node = edges.next[letterToIndex(letter)];
				}
			}
		}

        return trie;
    }
	
	List <Integer> solve (String text, int n, List <String> patterns) {
		List <Integer> result = new ArrayList <Integer> ();

		// write your code here
		List<Node> adjList = buildTrie(patterns);
		
		for(int i = 0; i < text.length(); ++i){
			Node currentNode = adjList.get(0);
			int j = i;
			char letter = text.charAt(j);
			while(currentNode.next[letterToIndex(letter)] != Node.NA){
				currentNode = adjList.get(currentNode.next[letterToIndex(letter)]);
				if(currentNode.next[letterToIndex('$')] != Node.NA){
					result.add(i);
					break;
				}
				if(currentNode.isLeave){
					break;
				}
				j++;
				if(j < text.length())
					letter = text.charAt(j);
				else break;
			}
		}

		return result;
	}

	public void run () {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
			String text = in.readLine ();
		 	int n = Integer.parseInt (in.readLine ());
		 	List <String> patterns = new ArrayList <String> ();
			for (int i = 0; i < n; i++) {
				patterns.add (in.readLine () + "$");
			}

			List <Integer> ans = solve (text, n, patterns);

			for (int j = 0; j < ans.size (); j++) {
				System.out.print ("" + ans.get (j));
				System.out.print (j + 1 < ans.size () ? " " : "\n");
			}
		}
		catch (Throwable e) {
			e.printStackTrace ();
			System.exit (1);
		}
	}

	public static void main (String [] args) {
		new Thread (new TrieMatchingExtended ()).start ();
	}
}
