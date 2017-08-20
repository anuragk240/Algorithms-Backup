import java.io.*;
import java.util.*;

import static java.lang.Math.*;

public class Closest {

    static class Point implements Comparable<Point> {
        long x, y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point o) {
            return o.y == y ? Long.signum(x - o.x) : Long.signum(y - o.y);
        }

        @Override
        public String toString(){
            return "(" + x + "," + y + ")";
        }
    }

    static double minimalDistance(int[] x, int y[]) {
        double ans = Double.POSITIVE_INFINITY;
        //write your code here
        Point[] points = new Point[x.length];
        for(int i = 0; i < x.length; ++i){
            points[i] = new Point(x[i], y[i]);
        }
        mergeSort(points, 0, points.length - 1, false);

        ans = divide(points, 0, points.length - 1);

        return ans;
    }

    private static double divide(Point[] data, int left, int right){
        double min = Double.POSITIVE_INFINITY;
        double d;
        if(right - left <= 3){
            for(int i = left; i <= right; ++i){
                for(int j = i + 1; j <= right; ++j){
					d = distance(data[i], data[j]);
					if(d < min) min = d;
				}
            }
        } else {
            int mid = (left + right) / 2;
			
            min = divide(data, left, mid);
            d = divide(data, mid + 1, right);

            if(d < min) min = d;
            double l, r;
			Point[] p = new Point[right - left + 1];
			int count = 0;
            for(int i = left; i <= right; ++i){
				if(Math.abs(data[i].y - data[mid].y) < min){
					p[count] = data[i];
					count++;
				}
			}
            d = merge(p, min, count);
            if(d < min) min = d;
        }

        return min;
    }

    private static double merge(Point[] points, double min, int count){
		mergeSort(points, 0, count - 1, true);
        
        double d = min, temp;
		int j;
        for(int i = 0; i < count; ++i){
			j = i + 1;
            while(j < count && points[j].x - points[i].x <= min){
                temp = distance(points[i], points[j]);
                if(temp < d)
                    d = temp;
				++j;
            }
        }
        return d;
    }

    private static double distance(Point a, Point b){
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }

    public static void main(String[] args) throws Exception {
		//stressTest();
		
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = new PrintWriter(System.out);
        int n = nextInt();
        int[] x = new int[n];
        int[] y = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = nextInt();
            y[i] = nextInt();
        }
        System.out.format("%.6f", minimalDistance(x, y));
        writer.close();
    }

    static BufferedReader reader;
    static PrintWriter writer;
    static StringTokenizer tok = new StringTokenizer("");


    static String next() {
        while (!tok.hasMoreTokens()) {
            String w = null;
            try {
                w = reader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (w == null)
                return null;
            tok = new StringTokenizer(w);
        }
        return tok.nextToken();
    }

    static int nextInt() {
        return Integer.parseInt(next());
    }

    private static void mergeSort(Point[] a, int left, int right, boolean isx) {
        if(left >= right) {
            return;
        } else {
            int mid = (left + right) / 2;
            mergeSort(a, left, mid, isx);
            mergeSort(a, mid + 1, right, isx);
            merge(a, left, right, isx);
        }
    }

    private static void merge(Point[] a, int left, int right, boolean isx) {
        int mid = (left + right) / 2, num = right - left + 1;
        Point[] temp = new Point[num + 1];
        int j = left, k = mid + 1, i = 0;
		
		if(isx){
			while(j <= mid && k <= right) {
				if (a[j].x < a[k].x) {        //a[j] < a[k]
					temp[i] = a[j];
					++j;
				} else {
					temp[i] = a[k];
					++k;
				}
				++i;
            }
		}else{
			while(j <= mid && k <= right) {
				if (a[j].compareTo(a[k]) < 0) {        //a[j] < a[k]
					temp[i] = a[j];
					++j;
				} else {
					temp[i] = a[k];
					++k;
				}
				++i;
			}
		}
		

        while(j <= mid){
            temp[i] = a[j];
            j++;
            i++;
        }

        while(k <= right){
            temp[i] = a[k];
            k++;
            i++;
        }

        for(i = 0; i < num; ++i){
            a[i + left] = temp[i];
        }
    }

	private static final int MAX = 2000000001;
	private static void stressTest(){
		System.out.println("Test started");
		int n = 100000;
		Random r = new Random();
		int[] x = new int[n];
		int[] y = new int[n];
		
		for(int i = 0; i < n; ++i){
			x[i] = r.nextInt(MAX) - MAX/2;
			y[i] = r.nextInt(MAX) - MAX/2;
		}
		System.out.println("Input generated");
		long startTime = System.nanoTime();
		System.out.format("%.6f \n", minimalDistance(x, y));
		long endTime = System.nanoTime();

		long duration = (endTime - startTime);
		System.out.format("Time = %.3f", (double)duration / 1000000000);
	}

}