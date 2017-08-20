import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SuffixArrayMatching {
    class fastscanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        fastscanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextint() throws IOException {
            return Integer.parseInt(next());
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

	private String buildBWT(int[] suffixArray, String text){
		StringBuffer bwt = new StringBuffer();
		for(int i = 0; i < suffixArray.length; ++i){
			bwt.append(text.charAt((suffixArray[i] + text.length() - 1) % text.length()));
		}
		//System.out.println("BWT = " + bwt.toString());
		return bwt.toString();
	}
	
	private void PreprocessBWT(String bwt, Map<Character, Integer> starts, Map<Character, int[]> occ_counts_before) {
        // Implement this function yourself
		int [] alphabets = new int[5];
		Arrays.fill(alphabets, 0);
		for(int i = 0; i < bwt.length(); ++i){
			alphabets[letterToIndex(bwt.charAt(i))]++;
		}
		
		int[] newalpha = new int[5];
		newalpha[0] = 0;
		for(int i = 1; i < alphabets.length; ++i){
			newalpha[i] += newalpha[i - 1] + alphabets[i - 1];
		}
		
		starts.put('$', newalpha[letterToIndex('$')]);
		starts.put('A', newalpha[letterToIndex('A')]);
		starts.put('T', newalpha[letterToIndex('T')]);
		starts.put('G', newalpha[letterToIndex('G')]);
		starts.put('C', newalpha[letterToIndex('C')]);
		
		int[] arr$ = new int[bwt.length() + 1];
		int[] arrA = new int[bwt.length() + 1];
		int[] arrC = new int[bwt.length() + 1];
		int[] arrG = new int[bwt.length() + 1];
		int[] arrT = new int[bwt.length() + 1];
		
		arr$[0] = arrA[0] = arrC[0] = arrG[0] = arrT[0] = 0;
		
		for(int i = 1; i <= bwt.length(); ++i){
			arr$[i] = arr$[i - 1];
			arrA[i] = arrA[i - 1];
			arrC[i] = arrC[i - 1];
			arrG[i] = arrG[i - 1];
			arrT[i] = arrT[i - 1];
			switch(bwt.charAt(i - 1)){
				case 'A':
					arrA[i]++;
					break;
				case 'C':
					arrC[i]++;
					break;
				case 'G':
					arrG[i]++;
					break;
				case 'T':
					arrT[i]++;
					break;
				case '$':
					arr$[i]++;
					break;
			}
		}
		
		occ_counts_before.put('$', arr$);
		occ_counts_before.put('A', arrA);
		occ_counts_before.put('C', arrC);
		occ_counts_before.put('G', arrG);
		occ_counts_before.put('T', arrT);
		
    }

    // Compute the number of occurrences of string pattern in the text
    // given only Burrows-Wheeler Transform bwt of the text and additional
    // information we get from the preprocessing stage - starts and occ_counts_before.
    void CountOccurrences(String pattern, String bwt, Map<Character, Integer> starts, Map<Character, int[]> occ_counts_before, int[] suffixArray, boolean[] occurs) {
        // Implement this function yourself
		int top = 0, bottom = bwt.length() - 1, i = pattern.length() - 1;
		while(top <= bottom && i >= 0){
			char c = pattern.charAt(i);
			top = starts.get(c) + occ_counts_before.get(c)[top];
			bottom = starts.get(c) + occ_counts_before.get(c)[bottom + 1] - 1;
			--i;
		}
		
		if(top > bottom){
			return;
		}else {
			for(int j = top; j <= bottom; ++j){
				occurs[suffixArray[j]] = true;
			}
		}
    }

    static public void main(String[] args) throws IOException {
        new SuffixArrayMatching().run();
    }

    public void print(boolean[] x) {
        for (int i = 0; i < x.length; ++i) {
            if (x[i]) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }

    public void run() throws IOException {
        fastscanner scanner = new fastscanner();
        String text = scanner.next() + "$";
        int[] suffixArray = computeSuffixArray(text);
        int patternCount = scanner.nextint();
        boolean[] occurs = new boolean[text.length()];
		Arrays.fill(occurs, false);
		
		String bwt = buildBWT(suffixArray, text);
		Map<Character, Integer> starts = new HashMap<Character, Integer>();
		Map<Character, int[]> occ_counts_before = new HashMap<Character, int[]>();
		PreprocessBWT(bwt, starts, occ_counts_before);
		
        for (int patternIndex = 0; patternIndex < patternCount; ++patternIndex) {
            String pattern = scanner.next();
			CountOccurrences(pattern, bwt, starts, occ_counts_before, suffixArray, occurs);
        }
        print(occurs);
    }
}
