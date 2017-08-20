import java.io.*;
import java.util.*;

public class HashSubstring {

    private static FastScanner in;
    private static PrintWriter out;

    public static void main(String[] args) throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        printOccurrences(getOccurrences(readInput()));
        out.close();
    }

    private static Data readInput() throws IOException {
        String pattern = in.next();
        String text = in.next();
        return new Data(pattern, text);
    }

    private static void printOccurrences(List<Integer> ans) throws IOException {
        for (Integer cur : ans) {
            out.print(cur);
            out.print(" ");
        }
    }

    private static List<Integer> getOccurrences(Data input) {
        String pattern = input.pattern, text = input.text;
        int plength = pattern.length(), tlength = text.length();
        List<Integer> occurrences = new ArrayList<Integer>();
		
		Hashing h = new Hashing();
		
		long phash = h.hash(pattern);
		long H[] = new long[tlength - plength + 1];
		long x = h.getX();
		long p = h.getPrime();
		
		H[H.length - 1] = h.hash(text.substring(tlength - plength));
		long mult = 1;
		for(int i = 0; i < plength; ++i){
			mult = (mult * x) % p;
		}
		
        for (int i = H.length - 2; i >= 0; --i) {
			H[i] = text.charAt(i) % p + ((x * H[i + 1]) % p + p) - (text.charAt(i + plength) * mult) % p;
			H[i] %= p;	
		}
		
		for(int i = 0; i < H.length; ++i){
			if(phash == H[i] && pattern.equals(text.substring(i, i + plength))){
				occurrences.add(i);
			}
			//System.out.print(H[i] + " ");
		}
		//System.out.println("\n" + H[H.length - 1] + " phash = " + phash + "\nx=" + String.valueOf(h.getX()));
		
        return occurrences;
    }
	
	public static class Hashing {
		private long x;
		private long prime = 1000000007;
		
		public Hashing(){
			Random r = new Random();
			x = (long)r.nextInt((int)prime - 1) + 1;
		}
		
		public long hash(String s){
			long h = 0;
			for(int i = s.length() - 1; i >= 0; --i){
				h = ((h * x) % prime + (long)s.charAt(i)) % prime;
			}
			
			return h;
		}
		
		public long getX(){
			return x;
		}
		
		public long getPrime(){
			return prime;
		}
	}

    static class Data {
        String pattern;
        String text;
        public Data(String pattern, String text) {
            this.pattern = pattern;
            this.text = text;
        }
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}

