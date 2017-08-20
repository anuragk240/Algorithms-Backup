import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class InverseBWT {
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

    String inverseBWT(String bwt) {
        StringBuilder result = new StringBuilder();

        // write your code here
		String lastColumn = bwt;
		
		int [] alphabets = new int[5];
		Arrays.fill(alphabets, 0);
		for(int i = 0; i < lastColumn.length(); ++i){
			alphabets[letterToIndex(lastColumn.charAt(i))]++;
		}
		
		int[] newalpha = new int[5];
		newalpha[0] = 0;
		for(int i = 1; i < alphabets.length; ++i){
			newalpha[i] += newalpha[i - 1] + alphabets[i - 1];
		}
		
		int[] lastToFirst = new int[lastColumn.length()];
		
		for(int i = 0; i < lastToFirst.length; ++i){
			lastToFirst[i] = newalpha[letterToIndex(lastColumn.charAt(i))]++;
		}
		
		int lastindex = 0;
		result.append('$');
		for(int i = 1 ; i < lastColumn.length(); ++i){
			char last = lastColumn.charAt(lastindex);
			result.append(last);
			lastindex = lastToFirst[lastindex];
		}
		
		result.reverse();

        return result.toString();
    }

    static public void main(String[] args) throws IOException {
        new InverseBWT().run();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String bwt = scanner.next();
        System.out.println(inverseBWT(bwt));
    }
}
