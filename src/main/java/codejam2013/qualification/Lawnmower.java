package codejam2013.qualification;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Lawnmower {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2013/qualification";

    private static final String SAMPLE = "B-sample.in";
    private static final String SMALL = "B-small-attempt2.in";
    private static final String LARGE = "B-large.in";

    private Scanner scanner;
    private PrintWriter writer;

    public Lawnmower(InputStream is, OutputStream os) {
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

        Lawnmower problem = new Lawnmower(is, os);
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

            // 1 <= N, M <= 100
            int n = scanner.nextInt();
            int m = scanner.nextInt();

            int a[][] = new int[n][m];
            for (int j=0; j<n; j++)
                for (int k=0; k<m; k++)
                    a[j][k] = scanner.nextInt();

            Lawn lawn = new Lawn(n, m, a);
            writer.println(lawn.solve() ? "YES" : "NO");
        }
    }

    private static class Lawn {
        private int n;
        private int m;

        private int a[][];
        private boolean confirmed[][];

        private Lawn(int n, int m, int[][] a) {
            this.n = n;
            this.m = m;
            this.a = a;

            initConfirmed();
        }

        // O(h*n*m) ~ 100*100*100 ~ 10^6
        private boolean solve() {
            for (int h=100; h>0; h--) {
                for (int i=0; i<n; i++) {
                    if (isRowConfirmed(i, h))
                        confirmRow(i, h);
                }

                for (int j=0; j<m; j++) {
                    if (isColumnConfirmed(j, h))
                        confirmColumn(j, h);
                }
            }
            return isAllConfirmed();
        }

        private void initConfirmed() {
            confirmed = new boolean[n][m];
            for (int i=0; i<n; i++)
                for (int j=0; j<m; j++)
                    confirmed[i][j] = false;
        }

        private boolean isAllConfirmed() {
            for (int i=0; i<n; i++)
                for (int j=0; j<m; j++)
                    if (!confirmed[i][j])
                        return false;
            return true;
        }

        private boolean isRowConfirmed(int row, int h) {
            for (int j=0; j<m; j++)
                if (a[row][j] > h)
                    return false;
            return true;
        }

        private boolean isColumnConfirmed(int column, int h) {
            for (int i=0; i<n; i++)
                if (a[i][column] > h)
                    return false;
            return true;
        }

        private void confirmRow(int row, int h) {
            for (int j=0; j<m; j++)
                if (a[row][j] == h)
                    confirmed[row][j] = true;
        }

        private void confirmColumn(int column, int h) {
            for (int i=0; i<n; i++)
                if (a[i][column] == h)
                    confirmed[i][column] = true;
        }
    }
}
