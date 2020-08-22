package algorithm.进阶;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class samemod {
    static File inF;
    static File outF;
    static PrintWriter write;
    static Scanner scanner;

    public static void main(String[] args) throws Exception {
        inF = new File("samemod.in");
        outF = new File("samemod.out");
        write = new PrintWriter(outF);
        scanner = new Scanner(inF);

        long m = scanner.nextLong();
        long n = scanner.nextLong();

        long x = getX(m,n);
        System.out.println(x);

        scanner.close();
        write.close();
    }
    //    9223372036854775807

    public static long getX(long m, long n) {
        long
                a = m,
                b = n,
                c = 0;
        while (true) {
            if ((a%b)*(++c %b) % b==1)
                break;
        }
        return c;
    }

}
