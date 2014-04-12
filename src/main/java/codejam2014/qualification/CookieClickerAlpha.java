package codejam2014.qualification;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Scanner;

public class CookieClickerAlpha {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2014/qualification";

    private static final String SAMPLE = "B-sample.in";
    private static final String SMALL = "B-small-attempt0.in";
    private static final String LARGE = "B-large.in";

    private Scanner scanner;
    private PrintWriter writer;

    public CookieClickerAlpha(InputStream is, OutputStream os) {
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

        CookieClickerAlpha problem = new CookieClickerAlpha(is, os);
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
        int t = scanner.nextInt();

        // 1 <= T <= 100
        for (int i = 1; i <= t; i++) {
            writer.print("Case #");
            writer.print(i + ": ");

            // 1 <= C <= 10000
            double c = scanner.nextDouble();

            // 1 <= F <= 100.
            double f = scanner.nextDouble();

            // 1 <= X <= 100000
            double x = scanner.nextDouble();

            writer.printf("%1$.7f\n", greedy(c, f, x));
        }
    }

    private BigDecimal greedy(double c, double f, double x) {
        BigDecimal t = BigDecimal.ZERO;
        double rate = 2;

        while(true) {
            double t1 = x / rate;
            double t2 = x / (rate + f) + c / rate;

            if (t1 > t2) {
                t = t.add(new BigDecimal(c / rate));
                rate += f;
            } else {
                return t.add(new BigDecimal(t1));
            }
        }
    }
}
