package algorithm.base;

import java.util.Scanner;

public class 单词长度 {
    public static void wordLength(String str) {
        int index = 0, sum = 0;

        for (int i = 0; i < str.length();i++ ) {
            if (str.charAt(i) == ' ') {
                while (i < str.length() && str.charAt(++i) == ' ') ;
                System.out.print(sum);
                if (index == 4) {
                    System.out.println();
                }else {
                    System.out.print(",");
                }
                sum = 1;
                index++;
            } else {
                sum++;
            }
        }
        System.out.print(sum);
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        String str = read.nextLine();
        wordLength(str);
    }
}
