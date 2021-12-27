package ua.kiev.prog.currencyRate;

import java.io.IOException;
import java.util.Scanner;

public abstract class MultiScanner {
    static final Scanner scan = new Scanner(System.in);

    public static String scan() throws IOException {
        String res = scan.nextLine();
        return res;
    }
}
