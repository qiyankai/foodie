package algorithm.进阶;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class GcdMeeTlcm {
    static File inF;
    static File outF;
    static PrintWriter write;
    static Scanner scanner;

    public static void main(String[] args) throws Exception {
        inF = new File("gcdMeeTlcm.in");
        outF = new File("gcdMeeTlcm.out");
        write = new PrintWriter(outF);
        scanner = new Scanner(inF);

        long m = scanner.nextLong();
        long n = scanner.nextLong();

        long gcd = gcd(m >= n ? m : n,
                m >= n ? n : m);

        long lcm = m * n / gcd;

        write.println(lcm);
        write.println(gcd);

        scanner.close();
        write.close();
    }

    public static long gcd(long m, long n) {
        long
                a = m,
                b = n,
                c = 0;
        while (true) {
            c = a % b;
            if (c == 0)
                break;
            a = b;
            b = c;
        }
        return b;
    }


}
