package algorithm.排序;

public class 选择排序 {
    public static void main(String[] args) {
        int[] arr = new int[]{59, 20, 17, 13, 28, 14, 23, 83};
        选择排序(arr);
        for (int i : arr) System.out.println(i);
    }

    public static void 选择排序(int a[]) {
        int i = 0;
        // 找到给定区域（i+1至数组末尾）最小的数字
        while (i < a.length) {
            int minNumIndex = i;
            int j = i;
            while (j < a.length) {
                if (a[j] < a[minNumIndex]) {
                    minNumIndex = j;
                }
                j++;
            }
            j = a[i];
            a[i] = a[minNumIndex];
            a[minNumIndex] = j;
            i++;
        }

    }
}
