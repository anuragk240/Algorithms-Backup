import java.util.*;
import java.io.*;

public class MajorityElement {
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

    private static int getMajorityElement(int[] a, int left, int right) {
        mergeSort(a, left, right - 1);
        int result = 1, num = right - left;
        
        for(int i = 1; i < num; ++i) {
            if(a[i] == a[i - 1]){
                result++;
                if(result > num / 2) return 1;
            } else {
                result = 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        FastScanner scanner = new FastScanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        if (getMajorityElement(a, 0, a.length) != -1) {
            System.out.println(1);
        } else {
            System.out.println(0);
        }
    }
    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner(InputStream stream) {
            try {
                br = new BufferedReader(new InputStreamReader(stream));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }
}

