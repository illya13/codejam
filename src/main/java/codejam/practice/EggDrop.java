package codejam.practice;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class EggDrop {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam/practice";

    private static final String SAMPLE = "C-sample-practice.in";
    private static final String SMALL = "C-small-practice.in";
    private static final String LARGE = "C-large-practice.in";

    private Scanner scanner;
    private PrintWriter writer;

    public EggDrop(InputStream is, OutputStream os) {
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

        EggDrop problem = new EggDrop(is, os);
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
    private static final long MAX_F_VALUE = 4294967296l;
    private static final int LARGE_F_VALUE = -1;
    private static final int MAX_B_INDEX = 32;

    private static final int F_CACHE_SIZE = 100000;
    private long[][] fCache;

    /**
     * Solve the problem
     */
    public void solve() {
        fCache = new long[F_CACHE_SIZE][MAX_B_INDEX];
        initFCache();

        int n = scanner.nextInt();

        for (int i = 1; i <= n; i++) {
            // int is enough to store all numbers
            // see Integer.MAX_VALUE, 2,000,000,000 < 2,147,483,647
            int F = scanner.nextInt();
            int D = scanner.nextInt();
            int B = scanner.nextInt();

            writer.print("Case #");
            writer.print(i + ": ");

            long Fmax = getF(D, B);
            int Dmin = getD(F, B, D);
            int Bmin = getB(F, D, B);

            writer.printf("%1$d %2$d %3$d\n", Fmax, Dmin, Bmin);
        }
    }

    /**
     * Get Fmax from F cache
     *
     * @param d D
     * @param b B
     * @return Fmax
     */
    private long getF(int d, int b) {
        if (b > MAX_B_INDEX)
            b = MAX_B_INDEX;

        if (b == 1)
            return d;

        if (d > F_CACHE_SIZE)
            return -1;

        return fCache[d - 1][b - 1];
    }

    /**
     * Get Dmin based on F cache
     *
     * @param f F
     * @param b B
     * @param dMax D
     * @return Dmin
     */
    private int getD(long f, int b, int dMax) {
        for (int d = 1; d <= dMax; d++) {
            long maxF = getF(d, b);
            if ((maxF == LARGE_F_VALUE) || (maxF >= f))
                return d;
        }
        throw new IllegalStateException(String.format("D not found, F=%1$d, B=%2$d, Dmax=%3$d", f, b, dMax));
    }

    /**
     * Get Bmin based on F cache
     *
     * @param f F
     * @param d D
     * @param bMax B
     * @return Bmin
     */
    private int getB(long f, int d, int bMax) {
        for (int b = 1; b <= bMax; b++) {
            long maxF = getF(d, b);
            if ((maxF == LARGE_F_VALUE) || (maxF >= f))
                return b;
        }
        throw new IllegalStateException(String.format("B not found, F=%1$d, D=%2$d, max B=%3$d", f, d, bMax));
    }

    /**
     * Init F cache. DP.<BR>
     * F(D, B) = F(D-1, B) + 1 + F(D-1, B-1)<BR>
     * if F(D, B) > 4294967296l then F(D, B) = -1
     */
    private void initFCache() {
        Arrays.fill(fCache[0], 1);

        for (int d = 1; d < F_CACHE_SIZE; d++) {
            fCache[d][0] = d + 1;
            for (int b = 1; b < MAX_B_INDEX; b++) {
                if ((fCache[d - 1][b] == LARGE_F_VALUE) || (fCache[d - 1][b - 1] == LARGE_F_VALUE))
                    fCache[d][b] = LARGE_F_VALUE;
                else {
                    fCache[d][b] = fCache[d - 1][b] + 1 + fCache[d - 1][b - 1];
                    if (fCache[d][b] >= MAX_F_VALUE)
                        fCache[d][b] = LARGE_F_VALUE;
                }
            }
        }
    }
}