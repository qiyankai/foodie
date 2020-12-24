package algorithm.进阶;

import java.util.Scanner;

public class YSF {
    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);

        int
                n = read.nextInt(),
                m = read.nextInt();
        String[] names = new String[n+2];
        for (int i=1; i<=n; i++){
            names[i] = read.nextLine();
        }
        int[] next = new int[n+2];
        for (int i=1; i<=n; i++){
            next[i]=i+1;
        }
        next[n]=1;
        int now = 1, last = n;
        for (int i=1; i<=n; i++){
            for (int j=1; j<=m-1; j++){
                last = now;
                now = next[now];
            }
            System.out.print(names[now]+" ");
            next[last] = next[now];
            now = next[now];
        }

    }
}
