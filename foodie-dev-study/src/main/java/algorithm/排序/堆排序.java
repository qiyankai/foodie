package algorithm.排序;

public class 堆排序 {
    public static void main(String[] args) {
        int[] arr = new int[]{ 73, 6, 72,88, 85, 83, 48, 60, 57, 42};
        MakeMaxHeap(arr, arr.length);
        for (int i : arr) System.out.print(i+",");
        System.out.println();
        MinHeap_Sort(arr, arr.length);
        for (int i : arr) System.out.print(i+",");
    }

    public static void MinHeap_Sort(int a[], int n) {
        int temp;
        MakeMaxHeap(a, n);
        int index=1;
        for (int i = n - 1; i >= 0; i--) {
            temp = a[0];
            a[0] = a[i];
            a[i] = temp;
            MaxHeapFixdown(a, 0, --n);

            System.out.print("第" + index++ + "次：" );
            for (int j = 0; j < a.length; j++) {
                if (j==n) System.out.print("|");
                System.out.print( a[j] + " ");
            }
            System.out.println();

        }
    }

    //构建最大堆
    public static void MakeMaxHeap(int a[], int n) {
        for (int i = (n - 1) / 2; i >= 0; i--) {
            MaxHeapFixdown(a, i, n);
        }
    }

    //从i节点开始调整,n为节点总数 从0开始计算 child节点的子节点为 2*parent+1, 2*parent+2
    public static void MaxHeapFixdown(int a[], int parent, int n) {
        int temp, child;
        while (parent * 2 + 1 <= n - 1) {
            child = parent * 2 + 1;
            if (child != n - 1 && a[child] < a[child + 1]) {
                child++;
            }
            if (a[parent] >= a[child]) {
                break;
            } else {
                temp = a[parent];
                a[parent] = a[child];
                a[child] = temp;
            }
            parent = child;
        }
    }

}
