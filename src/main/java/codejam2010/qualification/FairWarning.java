package codejam2010.qualification;

import java.io.*;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Scanner;
import java.util.TreeSet;

public class FairWarning {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2010/qualification";

    private static final String SAMPLE = "B-sample.in";
    private static final String SMALL = "B-small-attempt0.in";
    private static final String LARGE = "B-large.in";

    private Scanner scanner;
    private PrintWriter writer;

    public FairWarning(InputStream is, OutputStream os) {
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

        FairWarning problem = new FairWarning(is, os);
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
        int c = scanner.nextInt();

        for (int i = 1; i <= c; i++) {
            writer.print("Case #");
            writer.print(i + ": ");

            // 2 <= N <= 1000
            int n = scanner.nextInt();

            // 1 <= t[i] <= 10^50
            // collect t[i] into a TreeSet to have them sorted
            NavigableSet<BigInteger> t = new TreeSet<BigInteger>();
            for (int j = 0; j < n; j++)
                t.add(new BigInteger(scanner.next()));

            writer.println(solve(t));
        }
    }

    private BigInteger solve(NavigableSet<BigInteger> t) {
        // find GCD of all differences between
        Iterator i = t.descendingIterator();
        BigInteger prev = null;
        BigInteger div = null;
        while (i.hasNext()) {
            if (prev == null)
                prev = (BigInteger) i.next();
            else {
                BigInteger diff = prev.subtract((BigInteger) i.next()).abs();
                if (diff.equals(BigInteger.ZERO))
                    continue;
                if (div == null)
                    div = diff;
                else
                    div = div.gcd(diff);
            }
        }

        // calculate slarboseconds 
        BigInteger mod = prev.mod(div);
        if (mod.equals(BigInteger.ZERO))
            return BigInteger.ZERO;
        return div.subtract(mod);
    }
}