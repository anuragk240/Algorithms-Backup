import java.io.*;
import java.util.*;

public class JobQueue {
    private int numWorkers;
    private int[] jobs;

    private int[] assignedWorker;
    private long[] startTime;

    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new JobQueue().solve();
    }

    private void readData() throws IOException {
        numWorkers = in.nextInt();
        int m = in.nextInt();
        jobs = new int[m];
        for (int i = 0; i < m; ++i) {
            jobs[i] = in.nextInt();
        }
    }

    private void writeResponse() {
        for (int i = 0; i < jobs.length; ++i) {
            out.println(assignedWorker[i] + " " + startTime[i]);
        }
    }

    private void assignJobs() {
        // TODO: replace this code with a faster algorithm.
        assignedWorker = new int[jobs.length];
        startTime = new long[jobs.length];
		
		int temp;
		
		PriorityQueue pq = new PriorityQueue(numWorkers);
		long time = 0;
        for (int i = 0; i < jobs.length;) {
			if(pq.isFull()){
				time = pq.getMinFinishTime();
				pq.deleteJob();
				while(pq.getMinFinishTime() == time){
					pq.deleteJob();
				}
			} else {
				temp = pq.addJob(jobs[i] + time);
				assignedWorker[i] = temp;
				startTime[i] = time;
				++i;
			}
        }
    }
	
	private class PriorityQueue{
		
		private class Thread{
			final private int index;
			private long jobFinishTime;
			
			public Thread(int index){
				this.index = index;
				jobFinishTime = -1;
			}
			
			public void assignJob(long finishTime){
				jobFinishTime = finishTime;
			}
			
			public void deleteJob(){
				jobFinishTime = -1;
			}
			
			public int getIndex(){
				return index;
			}
			
			public long getFinishTime(){
				return jobFinishTime;
			}
		}
		
		private ArrayList<Thread> emptyThreads;
		private ArrayList<Thread> threadsWithJob;
		private int size;
		
		PriorityQueue(int size){
			this.size = size;
			emptyThreads = new ArrayList<>(size);
			threadsWithJob = new ArrayList<>(size);
			for(int i = 0; i < size; ++i){
				emptyThreads.add(new Thread(i));
			}
		}
		
		public boolean isFull(){
			if(threadsWithJob.size() == size)
				return true;
			else
				return false;
		}
		
		public boolean isEmpty(){
			if(threadsWithJob.size() == 0){
				return true;
			}
			else return false;
		}
		
		public int addJob(long finishTime){
			if(!isFull()){
				Thread t = dequeue(emptyThreads);
				t.assignJob(finishTime);
				enqueue(t, threadsWithJob);	
				return t.getIndex();
			}
			return -1;
		}
		
		public void deleteJob(){
			if(!isEmpty()){
				Thread t = dequeue(threadsWithJob);
				t.deleteJob();
				enqueue(t, emptyThreads);
			}
		}
		
		private void enqueue(Thread t, ArrayList<Thread> heap){
			heap.add(t);
			shiftUp(heap.size() - 1, heap);
		}
		
		private Thread dequeue(ArrayList<Thread> heap){
			Thread temp = heap.get(0);
			heap.set(0, heap.get(heap.size() - 1));
			heap.remove(heap.size() - 1);
			shiftDown(0, heap);
			return temp;
		}
		
		public long getMinFinishTime(){
			if(!isEmpty()) 
				return threadsWithJob.get(0).getFinishTime();
			else return -1;
		}
		
		private void shiftUp(int i, ArrayList<Thread> heap){
			int parent;
			long valuei, valuep;
			while(i >= 1){
				parent = (i - 1) / 2;
				if(heap.get(i).getFinishTime() != -1){
					valuei = heap.get(i).getFinishTime();
					valuep = heap.get(parent).getFinishTime();
				} else {
					valuei = heap.get(i).getIndex();
					valuep = heap.get(parent).getIndex();
				}
				if(valuei < valuep){
					Thread temp = heap.get(i);
					heap.set(i, heap.get(parent));
					heap.set(parent, temp);
					i = parent;
				} else {
					return;
				}
			}
		}
		
		private void shiftDown(int i, ArrayList<Thread> heap){
			int l = 2*i + 1, r = 2*i + 2;
			int index;
			long valuei, valuel, valuer, min;
		
			while(l < heap.size()){
				if(heap.get(i).getFinishTime() != -1){
					valuei = heap.get(i).getFinishTime();
					valuel = heap.get(l).getFinishTime();
				} else {
					valuei = heap.get(i).getIndex();
					valuel = heap.get(l).getIndex();
				}
				if(r < heap.size()){
					if(heap.get(r).getFinishTime() != -1){
						valuer = heap.get(r).getFinishTime();
					} else {
						valuer = heap.get(r).getIndex();
					}
					if(valuel < valuer) {
						min = valuel;
						index = l;
					} else {
						min = valuer;
						index = r;
					}
				} else {
					min = valuel;
					index = l;
				}
			
				if(valuei > min){
					Thread temp = heap.get(i);
					heap.set(i, heap.get(index));
					heap.set(index, temp);
					i = index;
					l = 2*i + 1;
					r = 2*i + 2;
				} else return;
			}
		}
	}

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        readData();
        assignJobs();
        writeResponse();
        out.close();
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