package algorithm.base;

import java.util.Scanner;

public class 潜伏者 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String p = scanner.nextLine();
        String r = scanner.nextLine();
        String s = scanner.nextLine();

        decode(p, r, s);
    }

    private static void decode(String p, String r, String s) {
        char[] arr = new char[26];
        for (int i = 0; i < p.length(); i++) {
            int pIndex = p.charAt(i) - 'A';
            int rIndex = r.charAt(i) - 'A';
            char pVal = p.charAt(i);
            char rVal = r.charAt(i);
            // 判断有无两个明文，对应同一个密文，检查密文、明文位置是否有值

            if (
                    (arr[pIndex] != '\u0000' && arr[pIndex] != rVal)
                    || (arr[rIndex] != '\u0000' && arr[rIndex] != pVal)
            ) {
                System.out.println("Failed");
                return;
            }
            arr[pIndex] = rVal;
            arr[rIndex] = pVal;
        }

        StringBuffer res = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char curr = arr[s.charAt(i) - 'A'];
            if (curr == '\u0000') {
                System.out.println("Failed");
                return;
            }
            res.append(curr);
        }
        System.out.println(res);
    }
}
