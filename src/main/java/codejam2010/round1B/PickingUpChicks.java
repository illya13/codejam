package codejam2010.round1B;

import java.io.*;
import java.util.Scanner;

public class PickingUpChicks {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2010/round1B";

    private static final String SAMPLE = "B-sample.in";
    private static final String SMALL = "B-small-attempt0.in";
    private static final String LARGE = "B-large.in";

    private Scanner scanner;
    private PrintWriter writer;

    public PickingUpChicks(InputStream is, OutputStream os) {
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

        PickingUpChicks problem = new PickingUpChicks(is, os);
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
    private static final String IMPOSSIBLE = "IMPOSSIBLE";

    private int n;
    private int k;
    private int b;
    private int t;
    private int x[];
    private int v[];

    /**
     * Solve the problem
     */
    public void solve() {
        int c = scanner.nextInt();

        for (int i = 1; i <= c; i++) {
            writer.print("Case #");
            writer.print(i + ": ");

            // number of chicks, 1 ? N ? 50, 0 ? K ? N;
            n = scanner.nextInt();
            k = scanner.nextInt();

            // location of the barn, 1 ? B ? 1,000,000,000;
            b = scanner.nextInt();

            // time, 1 ? T ? 1,000;
            t = scanner.nextInt();

            x = new int[n];
            for (int j=0; j<n; j++)
                x[j] = scanner.nextInt();

            v = new int[n];
            for (int j=0; j<n; j++)
                v[j] = scanner.nextInt();

            System.out.println();
            System.out.println();

            writer.println(simulate());
        }
    }

    private String simulate() {
        int[] xx = new int[n];
        int[] vv = new int[n];

        long swapCnt = 0;
        long chicks = 0;
        do {
            System.arraycopy(x, 0, xx, 0, n);
            System.arraycopy(v, 0, vv, 0, n);
            chicks = simulate(swapCnt, xx, vv);
            if (chicks >= k)
                return ((Long)swapCnt).toString();
            swapCnt++;
        } while (!isSorted(vv));
        return IMPOSSIBLE;
    }

    private long simulate(long maxSwaps, int[] xx, int[] vv) {
        long swapCnt = 0;
        for(int j=0; j<n; j++) {
            System.out.print(xx[j]);
            System.out.print(" ");
        }
        System.out.println();

        for(int i=0; i<t; i++) {
            if(isSorted(vv)) {
                for(int j=n-1; j>=0; j--)
                    xx[j] = xx[j]+(t-i)*vv[j];

                for(int j=0; j<n; j++) {
                    System.out.print(xx[j]);
                    System.out.print(" ");
                }
                System.out.println();

                break;
            }

            if ((swapCnt == maxSwaps) && isBlocked(xx, vv)) {
                int j=n-1;
                int jOld = xx[j];
                int vOld = vv[j];
                xx[j] = xx[j]+(t-i)*vv[j];
                j--;

                while(j>=0) {
                    if (xx[j] != jOld) {
                        jOld = xx[j];
                        vOld = vv[j];
                        xx[j] = xx[j]+(t-i)*vv[j];
                        j--;
                    }
                    else {
                        int jj=j;
                        while((jj >= 0) && (xx[jj] == jOld)) {
                            xx[jj] = xx[jj]+(t-i)*vOld;
                            jj--;
                        }
                        j=jj;
                    }
                }

                for(int jj=0; jj<n; jj++) {
                    System.out.print(xx[jj]);
                    System.out.print(" ");
                }
                System.out.println();

                break;
            }

            for(int j=n-1; j>=0; j--) {
                if (j==n-1)
                    xx[j] = xx[j]+vv[j];
                else {
                    if (xx[j]+vv[j] <= xx[j+1])
                        xx[j] = xx[j]+vv[j];
                    else
                        xx[j] = xx[j+1];

                    if ((xx[j]+vv[j] > xx[j+1]) && (vv[j] > vv[j+1]) && (swapCnt < maxSwaps)) {
                        swap(xx, j, j+1);
                        swap(vv, j, j+1);
                        swapCnt++;
                    }

                }
            }

            for(int j=0; j<n; j++) {
                System.out.print(xx[j]);
                System.out.print(" ");
            }
            System.out.println();
        }

        System.out.println();

        long reached = 0;
        for(int j=0; j<n; j++) {
            if (xx[j] >= b)
            reached++;
        }
        return reached;
    }

    private boolean isSorted(int a[]) {
        for(int i=0; i<a.length-1; i++)
            if (a[i] > a[i+1])
                return false;
        return true;
    }

    private boolean isBlocked(int a[], int b[]) {
        for(int i=0; i<a.length-1; i++)
            if ((b[i] > b[+1]) && (a[i] < a[i+1]))
                return false;
        return true;
    }

    private void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
}