import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class BuildHeap {
    private int[] data;
    private List<Swap> swaps;

    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new BuildHeap().solve();
    }

    private void readData() throws IOException {
        int n = in.nextInt();
        data = new int[n];
        for (int i = 0; i < n; ++i) {
          data[i] = in.nextInt();
        }
    }

    private void writeResponse() {
        out.println(swaps.size());
        for (Swap swap : swaps) {
          out.println(swap.index1 + " " + swap.index2);
        }
    }

    private void generateSwaps() {
		swaps = new ArrayList<Swap>();
		for(int i = data.length / 2; i >= 0; --i){
			shiftDown(i);
		}
    }
	
	private void shiftDown(int i){
		int l = 2*i + 1, r = 2*i + 2;
		int temp, min, index;
		
		while(l < data.length){
			if(r < data.length){
				if(data[l] < data[r]) {
					min = data[l];
					index = l;
				} else {
					min = data[r];
					index = r;
				}
			} else {
				min = data[l];
				index = l;
			}
			
			if(data[i] > min){
				swaps.add(new Swap(i, index));
				temp = data[i];
				data[i] = data[index];
				data[index] = temp;
				i = index;
				l = 2*i + 1;
				r = 2*i + 2;
			} else return;
		}
	}

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        readData();
        generateSwaps();
        writeResponse();
        out.close();
    }

    static class Swap {
        int index1;
        int index2;

        public Swap(int index1, int index2) {
            this.index1 = index1;
            this.index2 = index2;
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
