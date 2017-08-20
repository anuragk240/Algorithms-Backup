import java.util.*;

public class CoveringSegments {

    private static int[] optimalPoints(Segment[] segments) {
        //write your code here
        int[] points = new int[2 * segments.length];
        int answer = 0;
        segments = sort(segments.length, segments);

        int right;
        for(int i = 0, j = 0; i < segments.length; ++i){
            if(segments[i] != null){
                points[j++] = segments[i].end;
                ++answer;
                int k = i + 1;
                while(k < segments.length){
                    if(segments[k] != null && segments[k].start <= segments[i].end){
                        segments[k] = null;
                    }
                    ++k;
                }
                segments[i] = null;
            }
        }

        points[points.length - 1] = answer;
        return points;
    }

    private static class Segment {
        int start, end;

        Segment(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        for (int i = 0; i < n; i++) {
            int start, end;
            start = scanner.nextInt();
            end = scanner.nextInt();
            segments[i] = new Segment(start, end);
        }
        int[] points = optimalPoints(segments);
        int result = points[points.length - 1	];
        System.out.println(result);
        for (int i = 0; i < result; ++i) {
            System.out.print(points[i] + " ");
        }
    }

    public static Segment[] sort(int n_items, Segment[] s) {
        for(int i = 0; i < n_items; ++i){
        	for(int j = 0; j < i; ++j){
        		if(s[i].end < s[j].end){
        			swap(i, j, s);
        		}
        	}
        }
        return s;
    }
    
    public static void swap(int a, int b, Segment[] array){
    	   Segment temp = array[a];
    	   array[a] = array[b];
    	   array[b] = temp;
    }
}
 
