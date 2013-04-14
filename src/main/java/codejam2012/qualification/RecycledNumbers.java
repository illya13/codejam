package codejam2012.qualification;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class RecycledNumbers {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2012/qualification";

    private static final String SAMPLE = "C-sample.in";
    private static final String SMALL = "C-small-attempt0.in";
    private static final String LARGE = "C-large.in";

    private Scanner scanner;
    private PrintWriter writer;

    public RecycledNumbers(InputStream is, OutputStream os) {
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

        RecycledNumbers problem = new RecycledNumbers(is, os);
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

            int a = scanner.nextInt();
            int b = scanner.nextInt();

            writer.println(solve(a, b));
        }
    }

    private long solve(int a, int b) {
        long count = 0;
        for (int i=a; i<=b; i++) {
            char[] digits = getDigits(i);

            Set<Pair> pairs = new HashSet<Pair>();
            for (int j=0; j<digits.length; j++) {
                if (digits[0] == '0') {
                    shift(digits);
                    continue;
                }

                int number = getNumber(digits);
                shift(digits);

                if ((number < a) || (number > b)) {
                    continue;
                }

                if (number <= i) {
                    continue;
                }
                pairs.add(new Pair(i, number));
            }
            count += pairs.size();
        }
        return count;
    }

    private char[] getDigits(int i) {
        String string = ((Integer)i).toString();
        return string.toCharArray();
    }

    private int getNumber(char[] digits) {
        String string = new String(digits);
        return Integer.parseInt(string);
    }

    private void shift(char digits[]) {
        char first = digits[0];
        for (int i=1; i<digits.length; i++) {
            digits[i-1] = digits[i];
        }
        digits[digits.length-1] = first;
    }

    class Pair {
        int a;
        int b;

        Pair(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair pair = (Pair) o;

            if (a != pair.a) return false;
            if (b != pair.b) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = a;
            result = 31 * result + b;
            return result;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "a=" + a +
                    ", b=" + b +
                    '}';
        }
    }
}