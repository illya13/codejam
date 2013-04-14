package codejam2009.qualification;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class AlienLanguage {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2009/qualification";

    private static final String SAMPLE = "A-sample-practice.in";
    private static final String SMALL = "A-small-practice.in";
    private static final String LARGE = "A-large-practice.in";

    private Scanner scanner;
    private PrintWriter writer;

    public AlienLanguage(InputStream is, OutputStream os) {
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

        AlienLanguage problem = new AlienLanguage(is, os);
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
    private int l;
    private int d;
    private int n;

    private String[] words;

    /**
     * Solve the problem
     */
    public void solve() {
        l = scanner.nextInt();
        d = scanner.nextInt();
        n = scanner.nextInt();

        words = new String[d];
        for (int j=0; j<d; j++)
            words[j] = scanner.next();

        for (int i = 1; i <= n; i++) {
            writer.print("Case #");
            writer.print(i + ": ");

            String pattern = scanner.next();
            writer.println(solve(pattern));
        }
    }

    private Long solve(String pattern) {
        boolean inBrackets = false;

        Set<Character>[] charSet = new Set[l];
        for (int i = 0; i < l; i++)
            charSet[i] = new HashSet<Character>();

        int index = 0;
        for (int i = 0; i < pattern.length(); i++) {
            char ch = pattern.charAt(i);
            if (ch == '(') {
                inBrackets = true;
            } else if (ch == ')') {
                index++;
                inBrackets = false;
            } else {
                charSet[index].add(ch);
                if (!inBrackets)
                    index++;
            }
        }

        long count = 0;
        out:
        for (String word : words) {
            for (int i = 0; i < l; i++) {
                char ch = word.charAt(i);
                if (!charSet[i].contains(ch))
                    continue out;
            }
            count++;
        }
        return count;
    }

}