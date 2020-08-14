package algorithm.base;

import java.util.*;
import java.io.*;
import java.lang.*;

public class CalcuSky {
    static File inF;
    static File outF;
    static PrintWriter write;
    static Scanner scanner;

    public static void main(String[] args) throws Exception {
        inF = new File("CalcuSky.in");
        outF = new File("CalcuSky.out");
        write = new PrintWriter(outF);
//        scanner = new Scanner(inF);
        Scanner scanner = new Scanner(System.in);

        String m = scanner.nextLine();
        String n = scanner.nextLine();

        calcuSky(m.length() >= n.length() ? m : n,
                m.length() >= n.length() ? n : m);
        scanner.close();
        write.close();
    }

    //    9223372036854775807
    private static void calcuSky(String m, String n) {
        int flag = 0;
        int mLen = m.length();
        int nLen = n.length();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 1; i <= m.length(); i++) {
            int mCurr = m.charAt(mLen - i) - '0';
            int nCurr = i <= nLen ? n.charAt(nLen - i) - '0' : 0;
            int sum = mCurr + nCurr + flag;

            if (sum >= 10) {
                flag = 1;
            } else {
                flag = 0;
            }
            stringBuffer.append(sum % 10);
        }
        if (flag == 1) {
            stringBuffer.append(1);
        }
        write.println(stringBuffer.reverse());
    }

    private static void calcuSky18(String m, String n) {
        int flag = 0;
        int mLen = m.length();
        int nLen = n.length();
        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 18; i <= m.length(); i += 18) {

            int mStart = mLen-i-1;
            int mEnd = mLen-i+18-1;
            if (mStart<0){
                break;
            }
            long mCurrLong = Long.parseLong(m.substring(mStart, mEnd));
            int start = nLen-i-1;
            int end = nLen-i+18-1;
            if (start<0){
                break;
            }

            int mCurr = m.charAt(mLen - i) - '0';
            int nCurr = i <= nLen ? n.charAt(nLen - i) - '0' : 0;
            int sum = mCurr + nCurr + flag;

            if (sum >= 10) {
                flag = 1;
            } else {
                flag = 0;
            }
            stringBuffer.append(sum % 10);
        }
        if (flag == 1) {
            stringBuffer.append(1);
        }
        write.println(stringBuffer.reverse());
    }

}
