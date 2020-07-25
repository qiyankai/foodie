package algorithm.排序;

public class 归并排序 {
    public static void main(String[] args) {
        int[] arr = new int[]{59, 20, 17, 13, 28, 14, 23, 83};
        merge_sort(arr, 0, arr.length-1,new int[arr.length]);
        for (int i : arr) System.out.println(i);
    }

    public static void merge_sort(int a[], int first, int last, int temp[]) {

        if (first < last) {
            int middle = (first + last) / 2;
            merge_sort(a, first, middle, temp);//左半部分排好序
            merge_sort(a, middle + 1, last, temp);//右半部分排好序
            //合并左右部分
            int i, j, k;
            i = first;
            j = middle + 1;
            k = first;
            while (i <= middle && j <= last) {
                if (a[i] < a[j]) {
                    temp[k++] = a[i++];
                } else {
                    temp[k++] = a[j++];
                }
            }
            while (i <= middle){
                temp[k++] = a[i++];
            }
            while (j <= last){
                temp[k++] = a[j++];
            }
            for (int l = first; l < k; l++) {
                a[l] = temp[l];
            }
        }
    }

}
