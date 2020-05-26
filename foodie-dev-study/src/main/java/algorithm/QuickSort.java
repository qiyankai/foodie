package algorithm;

public class QuickSort {
    public static void main(String[] args) {
        int[] arr = new int[]{7, 2, 4, 8, 1, 3, 9, 10};
        quick(arr, 0, arr.length - 1);
        for (int i : arr) {
            System.out.print(i+",");
        }
    }

    public static void quick(int[] arr, int l, int r) {
        if (l < r) {
            int tmp = arr[l], i = l, j = r;
            while (i < j) {
                while (i < j && tmp <= arr[j])
                    j--;
                if (i < j)
                    arr[i++] = arr[j];
                while (i < j && arr[i] < tmp)
                    i++;
                if (i < j)
                    arr[j--] = arr[i];
            }
            arr[i] = tmp;
            quick(arr, l, i - 1);
            quick(arr, i + 1, r);
        }
    }

}
