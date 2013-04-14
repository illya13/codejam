package codejam2013.qualification;

import java.io.*;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class FairAndSquare {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2013/qualification";

    private static final String SAMPLE = "C-sample.in";
    private static final String SMALL = "C-small-attempt0.in";
    private static final String LARGE = "C-large-1.in";

    private Scanner scanner;
    private PrintWriter writer;

    public FairAndSquare(InputStream is, OutputStream os) {
        scanner = new Scanner(is);
        writer = new PrintWriter(os);
    }

    public void close() {
        scanner.close();
        writer.flush();
    }

    private static void runTest(String fileName, boolean isConsole) throws Exception {
        InputStream is = initInputStream(fileName);
        OutputStream os = initOutputStream(fileName, isConsole);

        FairAndSquare problem = new FairAndSquare(is, os);
        problem.solve();
        problem.close();

        doneStreams(isConsole, is, os);
    }

    private static InputStream initInputStream(String fileName) throws FileNotFoundException {
        File inputDir = new File(INPUT + File.separator + ROUND);
        File inputFile = new File(inputDir, fileName);
        InputStream is = new FileInputStream(inputFile);
        return is;
    }

    private static OutputStream initOutputStream(String fileName, boolean isConsole) throws FileNotFoundException {
        OutputStream os = System.out;
        if (isConsole) {
            System.out.println(fileName);
            System.out.println("          ---] cut [---");
        } else {
            File outputDir = new File(OUTPUT + File.separator + ROUND);
            outputDir.mkdirs();

            File outputFile = new File(outputDir, fileName.replace(".in", ".out"));
            os = new PrintStream(new FileOutputStream(outputFile));
        }
        return os;
    }

    private static void doneStreams(boolean isConsole, InputStream is, OutputStream os) throws IOException {
        is.close();
        if (isConsole) {
            System.out.println("          ---] cut [---");
            System.out.println("");
        } else {
            os.close();
        }
    }

    public static void main(String[] args) {
        try {
            runTest(SAMPLE, true);
            runTest(SMALL, false);
            runTest(LARGE, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // problem part

    /**
     * Solve the problem
     */
    public void solve() {
        List<BigInteger> list = init();

        int t = scanner.nextInt();

        // 1 <= T <= 10^000
        for (int i = 1; i <= t; i++) {
            writer.print("Case #");
            writer.print(i + ": ");

            // 1 <= A <= B <= 10^100
            BigInteger a = new BigInteger(scanner.next());
            BigInteger b = new BigInteger(scanner.next());

            writer.println(getCount(a, b, list));
        }
    }

    // O(10^8)
    private List<BigInteger> init() {
        List<BigInteger> list = new LinkedList<BigInteger>();

        BigInteger endpoint = (new BigInteger("10")).pow(8);
        BigInteger i = BigInteger.ONE;
        while(i.compareTo(endpoint) != 1) {
            if (isFair(i.toString())) {
                BigInteger square = i.pow(2);
                if (isFair(square.toString()))
                    list.add(square);
            }
            i = i.add(BigInteger.ONE);
        }

        return list;
    }

    private boolean isFair(String number) {
        for (int i=0; i<(number.length()+1)/2; i++)
            if(number.charAt(i) != number.charAt(number.length()-i-1))
                return false;
        return true;
    }

    private BigInteger getCount(BigInteger a, BigInteger b, List<BigInteger> list) {
        BigInteger count = BigInteger.ZERO;
        for (BigInteger bigInteger: list) {
            if (bigInteger.compareTo(b) == 1)
                break;
            if (bigInteger.compareTo(a) != -1)
                count = count.add(BigInteger.ONE);
        }
        return count;
    }
}