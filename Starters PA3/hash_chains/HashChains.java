import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class HashChains {

    private FastScanner in;
    private PrintWriter out;
    // store all strings in one list
    private ArrayList<ArrayList<String>> elems;
    // for hash function
    private int bucketCount;
    private long prime = 1000000007;
    private int multiplier = 263;

    public static void main(String[] args) throws IOException {
        new HashChains().processQueries();
    }

    private int hashFunc(String s) {
        long hash = 0;
        for (int i = s.length() - 1; i >= 0; --i)
            hash = (hash * multiplier + s.charAt(i)) % prime;
        return (int)(hash % (long)bucketCount);
    }

    private Query readQuery() throws IOException {
        String type = in.next();
        if (!type.equals("check")) {
            String s = in.next();
            return new Query(type, s);
        } else {
            int ind = in.nextInt();
            return new Query(type, ind);
        }
    }

    private void writeSearchResult(boolean wasFound) {
        out.println(wasFound ? "yes" : "no");
        // Uncomment the following if you want to play with the program interactively.
        // out.flush();
    }

    private void processQuery(Query query) {
		int hash, i;
		ArrayList<String> a;
		boolean found = false;
		
		if(!query.type.equals("check")){
			hash = hashFunc(query.s);
			a = elems.get(hash);
			for(i = 0; i < a.size(); ++i){
				if(a.get(i).equals(query.s)){
					found = true;
					break;
				}
			}
		}
		else{
			i = query.ind;
			a = elems.get(i);
		}
        switch (query.type) {
            case "add":
				if(!found) a.add(query.s);
                break;
            case "del":
				if(found) a.remove(i);
                break;
            case "find":
                writeSearchResult(found);
                break;
            case "check":          
				StringBuilder s = new StringBuilder();
				for(int j = 0; j < a.size(); ++j){
					s.insert(0, a.get(j) + " ");
				}
                out.println(s);
                // Uncomment the following if you want to play with the program interactively.
                // out.flush();
                break;
            default:
                throw new RuntimeException("Unknown query: " + query.type);
        }
    }

    public void processQueries() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        bucketCount = in.nextInt();
		elems = new ArrayList<ArrayList<String>>(bucketCount);
		for(int i = 0; i < bucketCount; ++i){
			elems.add(new ArrayList<String>());
		}
        int queryCount = in.nextInt();
        for (int i = 0; i < queryCount; ++i) {
            processQuery(readQuery());
        }
        out.close();
    }

    static class Query {
        String type;
        String s;
        int ind;

        public Query(String type, String s) {
            this.type = type;
            this.s = s;
        }

        public Query(String type, int ind) {
            this.type = type;
            this.ind = ind;
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