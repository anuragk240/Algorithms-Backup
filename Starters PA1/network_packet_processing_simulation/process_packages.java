import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class Request {
    public Request(int arrival_time, int process_time) {
        this.arrival_time = arrival_time;
        this.process_time = process_time;
		answer = 0;
    }

    public int arrival_time;
    public int process_time;
	public long answer;
}

class Response {
    public Response(boolean dropped, int start_time) {
        this.dropped = dropped;
        this.start_time = start_time;
    }

    public boolean dropped;
    public int start_time;
}

class Buffer {
	private int size;
    private Request[] queue;
	private int head;
	private int tail;
	
    public Buffer(int size) {
        this.size = size;
        queue = new Request[size + 1];
		head = 0;
		tail = 0;
    }
	
	public boolean addRequest(Request r){
		if(isFull()){
			return false;
		}
		queue[head++] = r;
		head %= size + 1;
		return true;
	}
	
	public Request getRequest(){
		if(isEmpty()){
			return null;
		}
		Request r = queue[tail];
		return r;
	}
	
	public boolean isEmpty(){
		return head == tail;
	}
	
	public boolean isFull(){
		if(tail - head == 1)
			return true;
		if (head == size && tail == 0)
			return true;
		return false;
	}
	
	public Request deleteRequest(){
		if(isEmpty()){
			return null;
		}
		Request r = getRequest();
		tail++;
		tail %= size + 1;
		return r;
	}
}

class process_packages {
    private static ArrayList<Request> ReadQueries(Scanner scanner) throws IOException {
        int requests_count = scanner.nextInt();
        ArrayList<Request> requests = new ArrayList<Request>();
        for (int i = 0; i < requests_count; ++i) {
            int arrival_time = scanner.nextInt();
            int process_time = scanner.nextInt();
            requests.add(new Request(arrival_time, process_time));
        }
        return requests;
    }

    private static void PrintResponses(ArrayList<Request> responses) {
        for (int i = 0; i < responses.size(); ++i) {
            Request response = responses.get(i);
            System.out.println(response.answer);
        }
    }
	
	public static ArrayList<Request> Process(ArrayList<Request> requests, Buffer buffer) {
        // write your code here
		if (requests.isEmpty()){
			return requests;
		}
		Request r = requests.get(0);
		buffer.addRequest(r);
		r.answer = r.arrival_time;
		
		long time = r.arrival_time + r.process_time;
		for(int i = 1; i < requests.size();){
			r = requests.get(i);
			while(r.arrival_time < time){
				if(!buffer.addRequest(r)){
					r.answer = -1;
				}
				if(++i < requests.size())
					r = requests.get(i);
				else break;
			}
			
			buffer.deleteRequest();
			if(!buffer.isEmpty()){
				buffer.getRequest().answer = time;
				time += buffer.getRequest().process_time;
			} else {
				if (i < requests.size()){
				buffer.addRequest(requests.get(i));
				time = requests.get(i).arrival_time + requests.get(i).process_time;
				requests.get(i).answer = requests.get(i).arrival_time;
				++i;
				}
			}
		}
		buffer.deleteRequest();
		while(!buffer.isEmpty()){
			buffer.getRequest().answer = time;
			time += buffer.getRequest().process_time;
			buffer.deleteRequest();
		}
        return requests;
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        int buffer_max_size = scanner.nextInt();
        Buffer buffer = new Buffer(buffer_max_size);

        ArrayList<Request> requests = ReadQueries(scanner);
        ArrayList<Request> responses = Process(requests, buffer);
        PrintResponses(responses);
    }
}
