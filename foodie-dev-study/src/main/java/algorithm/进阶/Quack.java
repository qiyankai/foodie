package algorithm.进阶;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Scanner;

public class Quack {
    static File inF;
    static File outF;
    static PrintWriter write;
    static Scanner scanner;

    public static void main(String[] args) throws FileNotFoundException {
//        inF = new File("Quack.in");
//        outF = new File("Quack.out");
//        write = new PrintWriter(outF);
//        scanner = new Scanner(inF);
        Scanner scanner = new Scanner(System.in);

        String str = scanner.nextLine();
        int
                q = 0,
                u = 0,
                a = 0,
                c = 0,
                k = 0;
        boolean
                die = false;
        for (char curr : str.toCharArray()) {
            switch (curr) {
                case 'q':
                    if (k>0){
                        k--;
                    }
                    q++;
                    break;
                case 'u':
                    if (q>0){
                        q--;
                        u++;
                    }else{
                        die = true;
                    }
                    break;
                case 'a':
                    if (u>0){
                        u--;
                        a++;
                    }else{
                        die =true;
                    }
                    break;
                case 'c':
                    if (a>0){
                        a--;
                        c++;
                    }else{
                        die = true;
                    }
                    break;
                case 'k':
                    if (c>0){
                        c--;
                        k++;
                    }else{
                        die = true;
                    }
                    break;
            }
            if (die){
                break;
            }
        }

        if (die || q>0 || u>0 || a>0 || c>0){
            System.out.println(-1);
        }else{
            System.out.println(k);
        }
    }
}
