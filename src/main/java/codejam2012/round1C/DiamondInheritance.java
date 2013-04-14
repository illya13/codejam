package codejam2012.round1C;

import java.io.*;
import java.util.*;

public class DiamondInheritance {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2012/round1C";

    private static final String SAMPLE = "A-sample.in";
    private static final String SMALL = "A-small-attempt0.in";
    private static final String LARGE = "A-large-practice.in";

    private Scanner scanner;
    private PrintWriter writer;

    public DiamondInheritance(InputStream is, OutputStream os) {
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

        DiamondInheritance problem = new DiamondInheritance(is, os);
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
            // runTest(LARGE, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // problem part
    private int n;
    private boolean[][] inherits;
    private boolean[] visited;

    /**
     * Solve the problem
     */
    public void solve() {
        int t = scanner.nextInt();

        for (int i = 1; i <= t; i++) {
            writer.print("Case #");
            writer.print(i + ": ");

            // 1 <= N <= 1000
            n = scanner.nextInt();
            inherits = new boolean[n][n];
            for (int j = 0; j < n; j++) {
                Arrays.fill(inherits[j], false);
                int m = scanner.nextInt();
                for (int k = 0; k < m; k++) {
                    inherits[j][scanner.nextInt()-1] = true;
                }
            }
            visited =  new boolean[n];
            Arrays.fill(visited, false);
            writer.println(solveInternal() ? "Yes" : "No");
        }
    }

    private boolean solveInternal() {
        boolean diamond = false;
        for (int i = 0; i < n; i++) {
            if (!visited[i])
                diamond |= visit(i);
        }
        return diamond;
    }

    private boolean visit(int i) {
        visited[i] = true;

        boolean diamond = false;
        for (int j = 0; j < n; j++) {
            if (inherits[i][j]) {
                if (!visited[j]) {
                    visit(j);
                }
                diamond |= merge(i, j);
            }
        }
        return diamond;
    }

    private boolean merge(int i, int j) {
        boolean present = false;
        for (int k = 0; k < n; k++) {
            present |= inherits[i][k] && inherits[j][k];
            inherits[i][k] |= inherits[j][k];
        }
        return present;
    }

    enum Status {
        NOT_VISITED,
        PENDING,
        VISITED
    }
}
