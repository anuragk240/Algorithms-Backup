import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BWMatching {
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
	
	int letterToIndex (char letter)
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

    // Preprocess the Burrows-Wheeler Transform bwt of some text
    // and compute as a result:
    //   * starts - for each character C in bwt, starts[C] is the first position
    //       of this character in the sorted array of
    //       all characters of the text.
    //   * occ_count_before - for each character C in bwt and each position P in bwt,
    //       occ_count_before[C][P] is the number of occurrences of character C in bwt
    //       from position 0 to position P inclusive.
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
    int CountOccurrences(String pattern, String bwt, Map<Character, Integer> starts, Map<Character, int[]> occ_counts_before) {
        // Implement this function yourself
		int top = 0, bottom = bwt.length() - 1, i = pattern.length() - 1;
		while(top <= bottom && i >= 0){
			char c = pattern.charAt(i);
			top = starts.get(c) + occ_counts_before.get(c)[top];
			bottom = starts.get(c) + occ_counts_before.get(c)[bottom + 1] - 1;
			--i;
		}
		
		if(top > bottom){
			return 0;
		}else {
			return bottom - top + 1;
		}
    }

    static public void main(String[] args) throws IOException {
        new BWMatching().run();
    }

    public void print(int[] x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String bwt = scanner.next();
        // Start of each character in the sorted list of characters of bwt,
        // see the description in the comment about function PreprocessBWT
        Map<Character, Integer> starts = new HashMap<Character, Integer>();
        // Occurrence counts for each character and each position in bwt,
        // see the description in the comment about function PreprocessBWT
        Map<Character, int[]> occ_counts_before = new HashMap<Character, int[]>();
        // Preprocess the BWT once to get starts and occ_count_before.
        // For each pattern, we will then use these precomputed values and
        // spend only O(|pattern|) to find all occurrences of the pattern
        // in the text instead of O(|pattern| + |text|).
        PreprocessBWT(bwt, starts, occ_counts_before);
        int patternCount = scanner.nextInt();
        String[] patterns = new String[patternCount];
        int[] result = new int[patternCount];
        for (int i = 0; i < patternCount; ++i) {
            patterns[i] = scanner.next();
            result[i] = CountOccurrences(patterns[i], bwt, starts, occ_counts_before);
        }
        print(result);
    }
}
