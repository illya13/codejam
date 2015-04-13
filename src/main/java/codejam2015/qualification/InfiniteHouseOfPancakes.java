package codejam2015.qualification;

import java.io.*;
import java.util.*;

public class InfiniteHouseOfPancakes {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2015/qualification";

    private static final String SAMPLE = "B-sample.in";
    private static final String SMALL = "B-small-practice.in";
    private static final String LARGE = "B-large-practice.in";

    private Scanner scanner;
    private PrintWriter writer;

    public InfiniteHouseOfPancakes(InputStream is, OutputStream os) {
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

        InfiniteHouseOfPancakes problem = new InfiniteHouseOfPancakes(is, os);
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

    private Map<ArrayList<Integer>, Integer> dpCache = new HashMap<ArrayList<Integer>, Integer>();

    /**
     * Solve the problem
     */
    public void solve() {
        int t = scanner.nextInt();

        // 1 <= T <= 100
        for (int i = 1; i <= t; i++) {
            writer.print("Case #");
            writer.print(i + ": ");

            // 1 <= D <= 1000
            // 1 <= Pi <= 1000
            int d = scanner.nextInt();
            ArrayList<Integer> plates = new ArrayList<Integer>(d);
            for (int j=0; j<d; j++)
                plates.add(scanner.nextInt());

            Collections.sort(plates);
            writer.printf("%1d\n", dp(plates));
        }
    }

    private int dp(final ArrayList<Integer> plates) {
        // try to move: (max, 2]
        int best = Collections.max(plates);
        for (int move = best-1; move >= 2; move--) {
            int current = move;
            for (int pancakes: plates) {
                current += (pancakes - 1) / move;
            }
            best = Math.min(best, current);
        }
        return best;
    }
}
