package algorithm;

public class 排序2020年11月27日 {
    public static void main(String[] args) {
        int[] arr = new int[]{7, 2, 4, 8, 1, 3, 9, 10};

        冒泡(arr);

        for (int i : arr) {
            System.out.print(i + ",");
        }
    }

    /**
     * 冒泡排序
     * 原理：遍历数组，从当前元素向后遍历，next比curr大，则交换位置，直至末尾
     * 时间复杂度    O（n^2）
     * 空间复杂度    1
     * <p>
     * 优化思路：    设置flag，当某次遍历未交换数据，停止
     *
     * @param arr
     */
    public static void 冒泡(int[] arr) {
        int temp;
        boolean flag;
        for (int i = 0; i < arr.length; i++) {
            flag = false;
            for (int j = arr.length - 1; j > i; j--) {
                if (arr[j - 1] > arr[j]) {
                    temp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = temp;
                    flag = true;
                }
            }
            if (!flag) break;
        }

    }

    /**
     * 选择排序
     * 原理：遍历数组，从当前元素向后遍历，选出最小的，与curr交换
     * 时间复杂度    O（n^2）
     * 空间复杂度    1
     * <p>
     * 优化思路：    设置flag，当某次遍历未交换数据，停止
     *
     * @param arr
     */
    public static void 选择(int[] arr) {
        int temp;
        boolean flag;
        for (int i = 0; i < arr.length; i++) {
            flag = false;
            for (int j = arr.length - 1; j > i; j--) {
                if (arr[j - 1] > arr[j]) {
                    temp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = temp;
                    flag = true;
                }
            }
            if (!flag) break;
        }

    }

}
