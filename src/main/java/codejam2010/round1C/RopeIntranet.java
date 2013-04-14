package codejam2010.round1C;

import java.io.*;
import java.util.Scanner;

public class RopeIntranet {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2010/round1C";

    private static final String SAMPLE = "A-sample.in";
    private static final String SMALL = "A-small-practice.in";
    private static final String LARGE = "A-large-practice.in";

    private Scanner scanner;
    private PrintWriter writer;

    public RopeIntranet(InputStream is, OutputStream os) {
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

        RopeIntranet problem = new RopeIntranet(is, os);
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

        for (int i = 1; i <= t; i++) {
            writer.print("Case #");
            writer.print(i + ": ");

            // 1 <= N <= 1000
            int n = scanner.nextInt();

            int a[] = new int[n];
            int b[] = new int[n];

            for (int j = 0; j < n; j++) {
                a[j] = scanner.nextInt();
                b[j] = scanner.nextInt();
            }

            writer.println(solve(n, a, b));
        }
    }

    private long solve(int n, int[] a, int b[]) {
        long result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (check(a[i], b[i], a[j], b[j]))
                    result++;
            }
        }
        return result;
    }

    private double getIntersect(double a1, double b1, double a2, double b2) {
        return ((a1 * (b2 - a2) - a2 * (b1 - a1))) / (b2 - a2 - b1 + a1);
    }

    private boolean isIntersect(int a1, int b1, int a2, int b2) {
        double y = getIntersect(a1, b1, a2, b2);
        return inRange(y, a1, b1) || inRange(y, a2, b2);
    }

    private boolean inRange(double y, int a1, int b1) {
        return (a1 != b1) &&
                (((a1 < y) && (y < b1)) || ((a1 > y) && (y > b1)));
    }

    private boolean isParallel(int a1, int b1, int a2, int b2) {
        return (b1 - a1) == (b2 - a2);
    }

    private boolean check(int a1, int b1, int a2, int b2) {
        return (!isParallel(a1, b1, a2, b2)) &&
                (isIntersect(a1, b1, a2, b2));
    }
}