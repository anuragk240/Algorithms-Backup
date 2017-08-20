import java.util.Scanner;

public class PointsAndSegments {

    static int count(int[] a, int[] b, int x) {
        int left = 0, right = a.length - 1, counta, countb;
        countb = search_b(b, x - 1, left, right) + 1;
        counta = a.length - search_a(a, x + 1, left, right);
        return counta + countb;
    }

    private static int search_a(int[] a, int x, int l, int r){
        int left = l, right = r, mid = (left + right)/2;
        if(left >= right){
            return left;
        }

        if (x <= a[mid]){
            return search_a(a, x, left, mid);
        } else {
            return search_a(a, x, mid + 1, right);
        }

    }

    private static int search_b(int[] a, int x, int l, int r){
        int left = l, right = r, mid = (left + right)/2 + 1;
        if(left >= right){
            return right;
        }

        if (x < a[mid]){
            return search_b(a, x, left, mid - 1);
        } else {
            return search_b(a, x, mid, right);
        }

    }

    private static final int MIN = -100000001;
    private static final int MAX = 100000001;

    private static int[] fastCountSegments(int[] starts, int[] ends, int[] points) {
        int[] cnt = new int[points.length];
        int[] a = new int[starts.length + 2];
        int[] b = new int[ends.length + 2];
        b[0] = a[0] = MIN;
        b[ends.length + 1] = a[starts.length + 1] = MAX;
        for(int i = 1; i < a.length - 1; ++i){
            a[i] = starts[i - 1];
            b[i] = ends[i - 1];
        }
        int total = a.length;
        //write your code here
        mergeSort(a, 0, a.length - 1);
        mergeSort(b, 0, b.length - 1);

        for(int i = 0; i < points.length; ++i) {
            cnt[i] = total - count(a, b, points[i]);
        }

        return cnt;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n, m;
        n = scanner.nextInt();
        m = scanner.nextInt();
        int[] starts = new int[n];
        int[] ends = new int[n];
        int[] points = new int[m];
        for (int i = 0; i < n; i++) {
            starts[i] = scanner.nextInt();
            ends[i] = scanner.nextInt();
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //use fastCountSegments
        int[] cnt = fastCountSegments(starts, ends, points);
        for (int x : cnt) {
            System.out.print(x + " ");
        }
    }

    private static void mergeSort(int[] a, int left, int right) {
        if(left >= right) {
            return;
        } else {
            int mid = (left + right) / 2;
            mergeSort(a, left, mid);
            mergeSort(a, mid + 1, right);
            merge(a, left, right);
        }
    }

    private static void merge(int[] a, int left, int right) {
        int mid = (left + right) / 2, num = right - left + 1;
        int[] temp = new int[num + 1];
        int j = left, k = mid + 1, i = 0;
        while(j <= mid && k <= right) {
            if (a[j] < a[k]) {
                temp[i] = a[j];
                ++j;
            } else {
                temp[i] = a[k];
                ++k;
            }
            ++i;
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

}

