package algorithm;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class 模板 {
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

        scanner.close();
        write.close();
    }
}
