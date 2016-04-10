package codejam2016.qualification;

import java.io.*;
import java.util.Scanner;

public class CountingSheep {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2016/qualification";

    private static final String SAMPLE = "A-sample.in";
    private static final String SMALL = "A-small-attempt0.in";
    private static final String LARGE = "A-large.in";

    private Scanner scanner;
    private PrintWriter writer;

    public CountingSheep(InputStream is, OutputStream os) {
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

        CountingSheep problem = new CountingSheep(is, os);
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

            // 0 ≤ N ≤ 10^6.
            int n = scanner.nextInt();
            writer.println(brute(n));
        }
    }

    private String brute(int n) {
        if (n == 0)
            return "INSOMNIA";

        boolean[] flags = new boolean[10];

        Long number = 0l;
        do {
            number += n;
            updateFlags(flags, number.toString());
        } while (!allFlags(flags));
        return number.toString();
    }

    private boolean allFlags(boolean[] flags) {
        for (boolean flag: flags) {
            if (!flag) {
                return false;
            }
        }
        return true;
    }

    private void updateFlags(boolean[] flags, String number) {
        for (char ch : number.toCharArray()) {
            flags[ch-48] = true;
        }
    }
}