package com.datav.job;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class 归并排序 {
//    public static void main(String[] args) {
//        int[] arr = new int[]{59, 20, 17, 13, 28, 14, 23, 83};
//        // 常规
////        merge_sort(arr, 0, arr.length - 1, new int[arr.length]);
//
//        // 多线程版本
//        System.out.println("活动cpu数量"+Runtime.getRuntime().availableProcessors());
//        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
//        MergeSortAction mergeSortAction = new MergeSortAction(0, arr.length - 1, arr, new int[arr.length]);
//        forkJoinPool.invoke(mergeSortAction);
//
//        for (int i : arr) System.out.println(i);
//    }

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
            while (i <= middle) {
                temp[k++] = a[i++];
            }
            while (j <= last) {
                temp[k++] = a[j++];
            }
            for (int l = first; l < k; l++) {
                a[l] = temp[l];
            }
        }
    }

    public static void print(int l,int r,int[] arr){
        for (int i = l; i <= r; i++) {
            System.out.print(arr[i]+",");
        }
        System.out.println();
    }

    public static void merge(int left, int right, int[] arr, int[] tempArr) {
        // 待排序
        System.out.println("-----------------");
        System.out.print("待排序：");
        print(left,right,arr);
        int middle = (left + right) / 2;
        for (int l = left, r = middle + 1, t = left; t <= right; t++) {
            if (l > middle) {
                tempArr[t] = arr[r++];
            } else if (r > right) {
                tempArr[t] = arr[l++];
            } else if (arr[l] > arr[r]) {
                tempArr[t] = arr[r++];
            } else {
                tempArr[t] = arr[l++];
            }
        }
        for (int i = left; i<=right; i++) {
            arr[i] = tempArr[i];
        }
        // 排序后
        System.out.print("排序后：");
        print(left,right,arr);
        System.out.println("-----------------");
    }

    static class MergeSortAction extends RecursiveAction {

        private int left;
        private int right;
        private int[] arrs;
        private int[] tempArr;

        public MergeSortAction(int left, int right, int[] arrs, int[] tempArr) {
            this.left = left;
            this.right = right;
            this.arrs = arrs;
            this.tempArr = tempArr;
        }

        @Override
        protected void compute() {
            System.out.println("开始"+left+"----"+right);
            if (right - left < 2) {
                return;
            }
            int middle = (left + right) / 2;
            MergeSortAction leftAction = new MergeSortAction(this.left, middle, arrs, tempArr);
            leftAction.fork();
            MergeSortAction rightAction = new MergeSortAction(middle + 1, this.right, arrs, tempArr);
            rightAction.compute();
            leftAction.join();
            merge(left, right,arrs,tempArr);
            System.out.println("结束"+left+"----"+right);
        }
    }


    public static void main(String[] args) {
        mergeSort();
    }

    public static void mergeSort() {
//        long[] arrs = new long[]{59, 20, 17, 13, 28, 14, 23, 83};

        long[] arrs = new long[5000000];
        for (int i = 0; i < 5000000; i++) {
            arrs[i] = (long) (Math.random() * 5000000);
        }

        long startTime = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        MergeSort mergeSort = new MergeSort(arrs);
        arrs = forkJoinPool.invoke(mergeSort);
//        arrs = mergeSort(arrs);
        long endTime = System.currentTimeMillis();
        System.out.println("耗时：" + (endTime - startTime));
//        for (long i : arrs) System.out.print(i+"、");
    }
    /**
     * fork/join
     * 耗时：13903ms
     */
    static class MergeSort extends RecursiveTask<long[]> {
        long[] arrs;
        public MergeSort(long[] arrs) {
            this.arrs = arrs;
        }
        @Override
        protected long[] compute() {
            if (arrs.length < 2) return arrs;
            int mid = arrs.length / 2;
            MergeSort left = new MergeSort(Arrays.copyOfRange(arrs, 0, mid));
            left.fork();
            MergeSort right = new MergeSort(Arrays.copyOfRange(arrs, mid, arrs.length));
            return merge(right.compute(), left.join());
        }
    }
    /**
     * 传统递归
     * 耗时：30508ms
     */
    public static long[] mergeSort(long[] arrs) {
        if (arrs.length < 2) return arrs;
        int mid = arrs.length / 2;
        long[] left = Arrays.copyOfRange(arrs, 0, mid);
        long[] right = Arrays.copyOfRange(arrs, mid, arrs.length);
        return merge(mergeSort(left), mergeSort(right));
    }
    public static long[] merge(long[] left, long[] right) {
        long[] result = new long[left.length + right.length];
        for (int i = 0, m = 0, j = 0; m < result.length; m++) {
            if (i >= left.length) {
                result[m] = right[j++];
            } else if (j >= right.length) {
                result[m] = left[i++];
            } else if (left[i] > right[j]) {
                result[m] = right[j++];
            } else result[m] = left[i++];
        }
        return result;
    }
}
