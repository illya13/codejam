package codejam2014.qualification;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class MagicTrick {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2014/qualification";

    private static final String SAMPLE = "A-sample.in";
    private static final String SMALL = "A-small-attempt0.in";
    private static final String LARGE = "A-large-1.in";

    private Scanner scanner;
    private PrintWriter writer;

    public MagicTrick(InputStream is, OutputStream os) {
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

        MagicTrick problem = new MagicTrick(is, os);
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

    /**
     * Solve the problem
     */
    public void solve() {
        int t = scanner.nextInt();

        // 1 <= T <= 100
        for (int i = 1; i <= t; i++) {
            writer.print("Case #");
            writer.print(i + ": ");

            int answer1 = scanner.nextInt();
            Arrangement arrangement1 = new Arrangement(scanner);

            int answer2 = scanner.nextInt();
            Arrangement arrangement2 = new Arrangement(scanner);

            writer.println(solveInternal(arrangement1.row(answer1), arrangement2.rowAsSet(answer2)));
        }
    }

    private String solveInternal(int[] row1, Set<Integer> set2) {
        int count = 0;
        Integer result = -1;
        for (int card: row1)
            if (set2.contains(card)) {
                result = card;
                count++;
            }

        if (count == 1)
            return result.toString();
        else if (count > 1)
            return "Bad magician!";
        else
            return "Volunteer cheated!";
    }

    private class Arrangement {
        private int cards[][];

        public Arrangement(Scanner scanner) {
            cards = new int[4][4];
            for (int i=0; i<4; i++)
                for (int j=0; j<4; j++)
                    cards[i][j] = scanner.nextInt();
        }

        public int[] row(int i) {
            return cards[i-1];
        }

        public Set<Integer> rowAsSet(int i) {
            Set<Integer> set = new HashSet<Integer>();
            for (int card: cards[i-1])
                set.add(card);
            return set;
        }
    }
}