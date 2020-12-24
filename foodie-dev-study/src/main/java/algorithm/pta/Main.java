package algorithm.pta;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);//生成Scanner对象

        int n = sc.nextInt();
        int[] arr = new int[n];
        for(int i = 0; i < n; i++){
            arr[i] = sc.nextInt();
        }
        int res = getMaxSumNum(arr);
        System.out.println(res);
        sc.close(); //关闭扫描器，是一个好的习惯
    }
     public static int getMaxSumNum(int[] strList){

        int temp = 0,maxNum=0;

        for(int i=0;i<strList.length;i++){
            temp +=strList[i];
            if(temp<0){
                temp = 0;
            }
            if(temp>maxNum){
                maxNum = temp;
            }
        }
        return maxNum;
    }

}

   