package algorithm;

public class 快速排序 {
    public static void main(String[] args) {
        int[] arr = new int[]{59, 20, 17, 13, 28, 14, 23, 83};
        quickSort(arr, 0, arr.length-1);
        for (int i : arr) System.out.println(i);
    }

    public static void quickSort(int a[], int l, int r) {
        if (l >= r) {
            return;
        }
        int i = l, j = r, key = a[l];
        while (i < j) {
            while (i < j && a[j] > key)
                j--;
            if (i < j)
                a[i++] = a[j];
            while (i < j && a[i] < key)
                i++;
            if (i < j)
                a[j--] = a[i];
        }
        // i==j
        a[i] = key;
        quickSort(a, l, i - 1);
        quickSort(a, i + 1, r);
    }
}
