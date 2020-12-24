package algorithm.排序;

import java.util.Arrays;

public class 快速排序 {
    public static void main(String[] args) {
        int[] arr = new int[]{59, 20, 17, 13, 28, 14, 23, 83};
//        quickSort(arr, 0, arr.length - 1);
        quickSortPro(arr, 0, arr.length - 1);
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


    public static void quickSortPro(int a[], int l, int r) {
        if (l >= r) {
            return;
        }
        // n中值法，取n个数字，中间那个作为主元，防在n-1，此时0，n-1，n有序
        findMedian(a, l, r);

        int i = l, j = r - 1, key = a[r-1];
        while (i < j) {
            while (i < j && a[--j] > key) ;
            while (i < j && a[++i] < key) ;
            if (i < j)
                swap(a, i, j);
            else
                break;
        }
        // i==j
        swap(a,++i,r-1);

        quickSort(a, l, i - 1);
        quickSort(a, i + 1, r);
    }

    private static void findMedian(int[] a, int l, int r) {
        int center = (l + r) / 2;
        if (a[l] > a[center]) {
            swap(a, l, center);
        }
        if (a[l] > a[r]) {
            swap(a, l, r);
        }
        if (a[center] > a[r]) {
            swap(a, r, center);
        }
        swap(a, center, r - 1);
    }

    public static void swap(int[] a, int source, int target) {
        if (source == target) return;
        int temp;
        temp = a[source];
        a[source] = a[target];
        a[target] = temp;
    }
}
