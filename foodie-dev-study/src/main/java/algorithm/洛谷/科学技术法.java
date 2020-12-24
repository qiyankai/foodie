package algorithm.洛谷;

import java.util.Scanner;

public class 科学技术法 {
    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        String str = read.nextLine();
        String[] s = str.split(" ");
        number(Integer.parseInt(s[0]), s[1]);
    }

    private static void number(int k, String x) {
        int length = x.length();

        StringBuffer stringBuffer = new StringBuffer();
        if (length <= k) {
            stringBuffer.append(1);
            for (int i = 1; i <= k - length; i++) {
                stringBuffer.append(0);
            }
            stringBuffer.append(x);
        } else {
            boolean flag = true;

            for (int i = length - k - 1; i >= 0; i--) {
                int curr = x.charAt(i) - '0';
                if (flag && ++curr == 10) {
                    stringBuffer.append(0);
                } else {
                    flag = false;
                    stringBuffer.append(curr);
                }
            }
            if (flag) {
                stringBuffer.append(1);
            }
            stringBuffer = stringBuffer.reverse();
            if (k>0) {
                String substring = x.substring(length - k, length );
                stringBuffer.append(substring);
            }

        }
        System.out.println(stringBuffer);
    }

}
