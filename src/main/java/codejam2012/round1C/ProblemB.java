package codejam2012.round1C;

import java.io.*;
import java.util.Scanner;

public class ProblemB {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2012/round1C";

    private static final String SAMPLE = "B-sample.in";
    private static final String SMALL = "B-small-practice.in";
    private static final String LARGE = "B-large-practice.in";

    private Scanner scanner;
    private PrintWriter writer;

    public ProblemB(InputStream is, OutputStream os) {
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

        ProblemB problem = new ProblemB(is, os);
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
            //runTest(SMALL, false);
            //runTest(LARGE, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // problem part

    private int l;
    private int p;
    private int c;

    /**
     * Solve the problem
     */
    public void solve() {
        int t = scanner.nextInt();

        for (int i = 1; i <= t; i++) {
            writer.print("Case #");
            writer.print(i + ": ");

            // L, P and C

            // 1 <= L < P <= 10^9
            // 2 <= C <= 10.

            l = scanner.nextInt();
            p = scanner.nextInt();
            c = scanner.nextInt();

            writer.println(binarySearch());
        }
    }

    private int binarySearch() {
        int start = 0;
        int end = (int) log(p / l, c);
        return binarySearch(start, end);
    }

    private int binarySearch(int start, int end) {
        int i = start + (end - start) / 2;

        long currC = (int) Math.pow(c, (int) Math.pow(2, i));
        long currP = l * currC;

        if (currP >= p) {
            if (start < i) {
                int result = binarySearch(start, i - 1);
                if (result != -1)
                    return result;
            }
            return i;
        }
        if (i < end)
            return binarySearch(i + 1, end);
        return -1;
    }

    private long log(long number, long base) {
        return (long) (Math.log(number) / Math.log(base));
    }
}