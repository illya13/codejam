package codejam2015.qualification;

import java.io.*;
import java.util.Scanner;

public class StandingOvation {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2015/qualification";

    private static final String SAMPLE = "A-sample.in";
    private static final String SMALL = "A-small-attempt0.in";
    private static final String LARGE = "A-large.in";

    private Scanner scanner;
    private PrintWriter writer;

    public StandingOvation(InputStream is, OutputStream os) {
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

        StandingOvation problem = new StandingOvation(is, os);
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

            // 0 <= Smax <= 1000
            int sMax = scanner.nextInt();
            int[] howMany = new int[sMax+1];

            String sLevels = scanner.next();
            for (int j=0; j<=sMax; j++)
                howMany[j] = sLevels.charAt(j) - 48;
            writer.println(greedy(sMax, howMany));
        }
    }

    private int greedy(int sMax, int[] howMany) {
        int additional = 0;

        int standing = howMany[0];
        for (int i=1; i<=sMax; i++) {
            if (i > standing) {
                additional += (i - standing);
                standing = i;
            }
            standing += howMany[i];
        }
        return additional;
    }
}