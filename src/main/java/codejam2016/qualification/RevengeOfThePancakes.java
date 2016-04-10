package codejam2016.qualification;

import java.io.*;
import java.util.*;

public class RevengeOfThePancakes {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2016/qualification";

    private static final String SAMPLE = "B-sample.in";
    private static final String SMALL = "B-small-attempt0.in";
    private static final String LARGE = "B-large.in";

    private Scanner scanner;
    private PrintWriter writer;

    public RevengeOfThePancakes(InputStream is, OutputStream os) {
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

        RevengeOfThePancakes problem = new RevengeOfThePancakes(is, os);
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

            String s = scanner.next();
            boolean[] happy = new boolean[s.length()];
            for (int j = 0; j < s.length(); j++) {
                happy[j] = (s.charAt(j) == '+');
            }
            writer.printf("%1d\n", greedy(happy));
        }
    }

    private long greedy(boolean[] happy) {
        long n = 0;
        while (true) {
            int i = happy.length - 1;
            while (happy[i]) {
                i--;
                if (i < 0)
                    return n;
            }

            for (int j = 0; j <= i; j++) {
                happy[j] = !happy[j];
            }

            n++;
        }
    }
}
