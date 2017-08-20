import java.util.*;
import java.io.*;
import java.util.zip.CheckedInputStream;

public class SuffixArrayLong {
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

    public class Suffix implements Comparable {
        String suffix;
        int start;

        Suffix(String suffix, int start) {
            this.suffix = suffix;
            this.start = start;
        }

        @Override
        public int compareTo(Object o) {
            Suffix other = (Suffix) o;
            return suffix.compareTo(other.suffix);
        }
    }
	
	private int letterToIndex (char letter)
	{
		switch (letter)
		{
			case '$': return 0;
			case 'A': return 1;
			case 'C': return 2;
			case 'G': return 3;
			case 'T': return 4;
			default: assert (false); return -1;
		}
	}
	
	private int[] initializeOrder(String text){
		int[] count = new int[5];  //Number of distinct symbols - 5
		Arrays.fill(count, 0);
		for(int i = 0; i < text.length(); ++i){
			++count[letterToIndex(text.charAt(i))];
		}
		for(int i = 1; i < count.length; ++i){
			count[i] += count[i - 1];
		}
		int[] order = new int[text.length()];
		for(int i = text.length() - 1; i >= 0; --i){
			order[--count[letterToIndex(text.charAt(i))]] = i;
		}
		
		return order;
	}
	
	private int[] computeEquivalenceClass(String text, int[] order){
		int[] eqClass = new int[text.length()];
		eqClass[order[0]] = 0;
		for(int i = 1; i < eqClass.length; ++i){
			if(text.charAt(order[i]) == text.charAt(order[i - 1]))
				eqClass[order[i]] = eqClass[order[i - 1]];
			else
				eqClass[order[i]] = eqClass[order[i - 1]] + 1;
		}
		
		return eqClass;
	}
	
	private int[] computeDoublePCS(int[] order, int[] eqClass, int length){
		int[] newOrder = new int[order.length];
		int[] count = new int[order.length];
		Arrays.fill(count, 0);
		
		for(int i = 0; i < order.length; ++i){
			++count[eqClass[order[i]]];
		}
		
		for(int i = 1; i < order.length; ++i){
			count[i] += count[i - 1];
		}
		
		int start;
		int eclass;
		for(int i = order.length - 1; i >= 0; --i){
			start = (order[i] - length + order.length) % order.length;
			eclass = eqClass[start];
			newOrder[--count[eclass]] = start;
		}
		
		return newOrder;
	}
	
	private int[] updateEqClass(int[] order, int[] eqClass, int length){
		int[] newEqClass = new int[eqClass.length];
		
		newEqClass[order[0]] = 0;
		int cur1, cur2, prev1, prev2;
		for(int i = 1; i < order.length; ++i){
			prev1 = eqClass[order[i - 1]];
			prev2 = eqClass[(order[i - 1] + length) % order.length];
			cur1 = eqClass[order[i]];
			cur2 = eqClass[(order[i] + length) % order.length];
			
			if((cur1 == prev1) && (cur2 == prev2)){
				newEqClass[order[i]] = newEqClass[order[i - 1]];
			}else {
				newEqClass[order[i]] = newEqClass[order[i - 1]] + 1;
			}
		}
		
		return newEqClass;
	}
	
	

    // Build suffix array of the string text and
    // return an int[] result of the same length as the text
    // such that the value result[i] is the index (0-based)
    // in text where the i-th lexicographically smallest
    // suffix of text starts.
    public int[] computeSuffixArray(String text) {
        // write your code here
		int[] order = initializeOrder(text);
		int[] eqClass = computeEquivalenceClass(text, order);
		
		int length = 1;
		while(length < text.length()){
			order = computeDoublePCS(order, eqClass, length);
			eqClass = updateEqClass(order, eqClass, length);
			length *= 2;
		}

        return order;
    }


    static public void main(String[] args) throws IOException {
        new SuffixArrayLong().run();
    }

    public void print(int[] x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        int[] suffix_array = computeSuffixArray(text);
        print(suffix_array);
    }
}
