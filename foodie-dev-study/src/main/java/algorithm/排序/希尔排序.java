package algorithm.排序;

public class 希尔排序 {
    public static void main(String[] args) {
        int[] arr = new int[]{59, 20, 17, 13, 28, 14, 23, 83};
        shellSort(arr, arr.length);
        for (int i : arr) System.out.println(i);
    }
    public static void shellSort(int array[], int lenth) {
        int temp = 0;
        int 间隔 = lenth;
        do {
            间隔 /= 2;
            // 控制序列
            for (int k = 0; k < 间隔; k++) {
                // 循环序列-根据间隔
                for (int i = k + 间隔; i < lenth; i += 间隔) {
                    // 根据当前间隔，依次插入排序
                    for (int j = i; j > k; j -= 间隔) {
                        if (array[j] < array[j - 间隔]) {
                            temp = array[j];
                            array[j] = array[j - 间隔];
                            array[j - 间隔] = temp;
                        }
                    }
                }
            }
        } while (间隔 != 1);
    }
}
