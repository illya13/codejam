package codejam2016.qualification;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class CoinJam {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2016/qualification";

    private static final String SAMPLE = "C-sample.in";
    private static final String SMALL = "C-small-practice.in";
    private static final String LARGE = "C-large.in";

    private Scanner scanner;
    private PrintWriter writer;

    public CoinJam(InputStream is, OutputStream os) {
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

        CoinJam problem = new CoinJam(is, os);
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
            // runTest(SMALL, false);
            // runTest(LARGE, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // problem part

    private boolean[] isPrime;
    private int[] multiples;

    /**
     * Solve the problem
     */
    public void solve() {
        initPrimes(100000000);

        // 1 <= T <= 1
        int t = scanner.nextInt();

        for (int i = 1; i <= t; i++) {
            writer.print("Case #");
            writer.println(i + ": ");

            int n = scanner.nextInt();
            int j = scanner.nextInt();

            brute(n, j);
       }
    }

    private void brute(int n, int j) {
        number:
        for (int i=0; i < 100; i++) {
            BigInteger bi = BigInteger.valueOf(i);
            BigInteger jamcoin = new BigInteger("1" + bi.toString(2) + "1", 2);

            for (int r=2; r<=10; r++) {
                int value = Integer.valueOf(jamcoin.toString(r));
                if (isPrime[value]) {
                    continue number;
                }
            }
            System.out.println(jamcoin.toString(2));
        }
    }

    private static long toBase(long n, long b) {
        StringBuilder number = new StringBuilder();
        for (int i = (int) log(n, b); i >= 0; i--) {
            long power = (long) Math.pow(b, i);
            int index = (int) (n / power);
            number.append(index);
            n -= index * power;
        }
        return Long.parseLong(number.toString());
    }

    private static long log(long number, long base) {
        return (long) (Math.log(number) / Math.log(base));
    }

    private void initPrimes(int n) {
        // initially assume all integers are prime
        isPrime = new boolean[n + 1];
        multiples = new int[n + 1];
        for (int i = 2; i <= n; i++) {
            isPrime[i] = true;
        }

        // mark non-primes <= N using Sieve of Eratosthenes
        for (int i = 2; i*i <= n; i++) {
            // if i is prime, then mark multiples of i as nonprime
            // suffices to consider mutiples i, i+1, ..., N/i
            if (isPrime[i]) {
                for (int j = i; i*j <= n; j++) {
                    isPrime[i*j] = false;
                    multiples[i*j] = i;
                }
            }
        }
    }
}