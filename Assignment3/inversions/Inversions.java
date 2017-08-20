import java.util.*;

public class Inversions {
    private static long mergeSort(int[] a, int left, int right) {
        if(left >= right) {
            return 0;
        } else {
            int mid = (left + right) / 2;
            return mergeSort(a, left, mid) +
            mergeSort(a, mid + 1, right) +
            merge(a, left, right);
        }
    }

    private static long merge(int[] a, int left, int right) {
        long answer = 0;
        int mid = (left + right) / 2, num = right - left + 1;
        int[] temp = new int[num + 1];
        int j = left, k = mid + 1, i = 0, n = k - left;
        while(j <= mid && k <= right) {
            if (a[j] <= a[k]) {
                temp[i] = a[j];
                ++j;
                n--;
            } else {
                answer += n;
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
        return answer;
    }

    private static long getNumberOfInversions(int[] a, int[] b, int left, int right) {
        long numberOfInversions = mergeSort(a, 0, a.length - 1);
		for(int i =0 ; i < a.length; ++i){
			System.out.print(a[i] + " ");
		}
		System.out.println();
        return numberOfInversions;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int[] b = new int[n];
        System.out.println(getNumberOfInversions(a, b, 0, a.length));
    }
}

