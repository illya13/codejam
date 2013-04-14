package codejam2012.qualification;

import java.io.*;
import java.util.*;

public class DancingWithTheGooglers {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2012/qualification";

    private static final String SAMPLE = "B-sample.in";
    private static final String SMALL = "B-small-attempt0.in";
    private static final String LARGE = "B-large.in";

    private Scanner scanner;
    private PrintWriter writer;

    public DancingWithTheGooglers(InputStream is, OutputStream os) {
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

        DancingWithTheGooglers problem = new DancingWithTheGooglers(is, os);
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

            int n = scanner.nextInt();
            int s = scanner.nextInt();
            int p = scanner.nextInt();

            int[] googlers = new int[n];

            for (int j = 0; j < n; j++) {
                googlers[j] = scanner.nextInt();
            }

            writer.println(solve(s, p, googlers));
        }
    }

    private int solve(int s, int p, int[] googlers) {
        List<Integer> withoutSurprize = new LinkedList<Integer>();
        List<Integer> withSurprize = new LinkedList<Integer>();
        for(int g: googlers){
            int two = g-p;
            if(two < 0)
                continue;

            if (two >= 2*(p-1)) {
                withoutSurprize.add(g);
            } else {
                if( two >= 2*(p-2)) {
                    withSurprize.add(g);
                }
            }
        }
        int ress = withoutSurprize.size();
        int ws = (withSurprize.size() <= s) ? withSurprize.size() : s;
        return ress += ws;
    }
}