//package algorithm.动态规划;
//
//import java.util.*;
//import java.lang.*;
//import java.io.*;
//
//public class Main {
//
//
//    private static void build(int[] tree, int[] a,int p, int l, int r){
//        if (l==r){
//            tree[p]=a[l];
//            // back[l]=p;
//            return;
//        }
//        int mid=(l+r)/2;
//        build(tree,a,p+p,l,mid);
//        build(tree,a,p+p+1,mid+1,r);
//        tree[p]=tree[p+p]+tree[p+p+1];
//        // treeBigger[p]= Math.max(treeBigger[p+p], treeBigger[p+p+1])
//    }
//
//    private static int sum(int[] tree,int p,int l, int r, int x, int y){
//        if (l==x && r==y)
//            return tree[p];
//        int mid=(l+r)/2;
//        if (y<=mid)
//            return sum(tree,p+p,l,mid,x,y);
//        if (mid+1<=x)
//            return sum(tree,p+p+1,mid+1,r,x,y);
//        if (x<=mid && mid+1<=y)
//            return sum(tree,p+p,l,mid,x,mid) + sum(tree,p+p+1,mid+1,r,mid+1,y);
//        //  return Math.max( sum(tree,p+p,l,mid,x,mid) , sum(tree,p+p+1,mid+1,r,mid+1,y) );
//    }
//
//    private static void change(int[] tree, int p, int l, int r, int k, int addValue){
//        if (l==k && r==k){
//            tree[p]+=addValue;
//            return;
//        }
//        int mid=(l+r)/2;
//        tree[p]+=addValue;
//        if (k<=mid){
//            change(tree,p+p,l,mid,k,addValue);
//        }else{
//            change(tree, p+p+1,mid+1,r,k,addValue);
//        }
//        return;
//    }
//
//
//
//    /*
//    *
//    * 把原问题分解为 若干个 形式相同 规模较小的子问题
//    * 解决着若干个子问题
//    *           当子问题足够小，基本情况，直接求解
//    *               否则 递归调用 朝更简单的子问题前进一步
//    *
//    * 1、判断子问题是否足够小
//    * 2、递归调用 朝更简单的子问题前进一步
//    * 3、合并子问题的解，得到原问题的解
//    *
//    * */
//
//    public static void main(String[] args) {
//	// write your code here
//        Scanner read = new Scanner(System.in);
//
//        int n = read.nextInt();
//        int[] tree,a = new int[10001];
//        build(tree, a,1,1, n);
////        System.out.print(aa(f, n));
//
//        int x,y;
//        sum(tree,1,1,n,x,y);
//
//
//
//
//
//        read.close();
//    }
//}
//
////for
////fenzhicelue
////mimameiju
////feibonaqi
////xianduanshu
