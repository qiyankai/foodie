package algorithm;

//链接：https://www.nowcoder.com/questionTerminal/bbfc7fb56a07415ab2b94402379f7e56
//来源：牛客网

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
 
// complexity O(N)
public class ReverseList {
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        int N = 0, M = 0;
        int[] A = null;
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                String[] temp = input.readLine().split("\\s");
                N = Integer.parseInt(temp[0]);
                M = Integer.parseInt(temp[1]);
            }
            else {
                String[] temp = input.readLine().split("\\s");
                A = new int[N];
                for (int j = 0; j < N; j++) {
                    A[j] = Integer.parseInt(temp[j]);
                }
            }
        }
        input.close();
        A = moveA(A, M);
        printer(A);
    }
 
    private static int[] moveA(int[] A, int M) {
        M %= A.length;
        if (M == 0)
            return A;
        reverse(A, 0, A.length - 1);
        reverse(A, 0, M - 1);
        reverse(A, M, A.length - 1);
        return A;
    }
 
    // reverse array from start to end
    private static void reverse(int[] a, int start, int end) {
        for (int i = start, j = end; i <= (start + end) / 2; i++, j--) {
            swap(a, i, j);
        }
    }
 
    private static void swap(int[] a, int left, int right) {
        int temp = a[left];
        a[left] = a[right];
        a[right] = temp;
    }
 
    private static void printer(int[] A) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < A.length; i++) {
            sb.append(A[i]);
            sb.append(" ");
        }
 
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb.toString());
    }
}