package algorithm.base;

import java.util.Scanner;

public class 单词长度 {
//    public static void wordLength(String str) {
//        int index = 0, sum = 0;
//
//        for (int i = 0; i < str.length();i++ ) {
//            if (str.charAt(i) == ' ') {
//                while (i < str.length() && str.charAt(++i) == ' ') ;
//                if(sum!=0) {
//                    System.out.print(sum);
//                    if (index == 4) {
//                        System.out.println();
//                    } else {
//                        System.out.print(",");
//                    }
//
//                    index++;
//                }
//                sum = 1;
//            } else {
//                sum++;
//            }
//        }
//        if(sum!=0) System.out.println(sum);
//    }

    private static void wordLength(String str){

        str+=" @"; int pos=0;
        boolean hasWord = false;
        char nowChar='A';
        int sum = 0;
        int countWords = 0;

        while ( (nowChar=str.charAt(pos) ) !='@' ){

            if (nowChar==' '){
                if ( hasWord ){
                    System.out.print(sum);
                    sum = 0;
                    hasWord = false;
                    countWords++;
                }
            }else{
                if ( hasWord == false && countWords!=0 ){
                    System.out.printf(  "%s" , (countWords%5!=0) ? ",":"\n" );
                }
                hasWord = true;
                sum++;
            }
            pos++;

        }

    }

    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        String str = read.nextLine();
        wordLength( str );
    }
}
