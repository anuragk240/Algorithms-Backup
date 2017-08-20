import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BurrowsWheelerTransform {
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

    String BWT(String text) {
        StringBuilder result = new StringBuilder();

        // write your code here
		String[] matrix = new String[text.length()];
		StringBuilder temp = new StringBuilder(text);
		matrix[0] = text;
		for(int i = 1; i < matrix.length; ++i){
			char firstChar = temp.charAt(0);
			temp.deleteCharAt(0);
			temp.append(firstChar);
			matrix[i] = temp.toString();
		}
		
		Comparator<String> comparator = null;
		comparator = new Comparator<String>(){
			@Override
			public int compare(String s1, String s2){
				return s1.compareTo(s2);
			}
		};
		
		Arrays.sort(matrix, comparator);
		
		for(int i = 0; i < matrix.length; ++i){
			result.append(matrix[i].charAt(text.length() - 1));
		}

        return result.toString();
    }
	
	

    static public void main(String[] args) throws IOException {
        new BurrowsWheelerTransform().run();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        System.out.println(BWT(text));
    }
}
