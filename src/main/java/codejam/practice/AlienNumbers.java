package codejam.practice;

import java.io.*;
import java.util.Scanner;

public class AlienNumbers {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam/practice";

    private static final String SAMPLE = "A-sample-practice.in";
    private static final String SMALL = "A-small-practice.in";
    private static final String LARGE = "A-large-practice.in";

    private Scanner scanner;
    private PrintWriter writer;

    public AlienNumbers(InputStream is, OutputStream os) {
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

        AlienNumbers problem = new AlienNumbers(is, os);
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
        int n = scanner.nextInt();

        for (int i = 1; i <= n; i++) {
            writer.print("Case #");
            writer.print(i + ": ");

            char[] alienNumber = scanner.next().toCharArray();
            char[] sourceLanguage = scanner.next().toCharArray();
            char[] targetLanguage = scanner.next().toCharArray();

            // long is enough to store sequence number
            // see Long.MAX_VALUE (9223372036854775807 ~ 10^19)
            long sequenceNumber = getSequenceNumber(alienNumber, sourceLanguage);
            char target[] = getAlienNumber(sequenceNumber, targetLanguage);

            writer.println(target);
        }
    }

    /**
     * Gets sequence number of a alien_number in alien language
     *
     * @param alienNumber   alien number
     * @param alienLanguage alien language
     * @return sequence number
     */
    private long getSequenceNumber(char[] alienNumber, char[] alienLanguage) {
        long result = 0;
        for (int i = 0; i < alienNumber.length; i++) {
            for (int j = 0; j < alienLanguage.length; j++) {
                if (alienNumber[alienNumber.length - i - 1] == alienLanguage[j]) {
                    result += (long) Math.pow(alienLanguage.length, i) * j;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Gets alien_number by a sequence number in alien language
     *
     * @param sequenceNumber sequence number
     * @param alienLanguage  alien language
     * @return alien number
     */
    private char[] getAlienNumber(long sequenceNumber, char[] alienLanguage) {
        StringBuilder alienNumber = new StringBuilder();

        for (int i = (int) log(sequenceNumber, alienLanguage.length); i >= 0; i--) {
            long power = (long) Math.pow(alienLanguage.length, i);
            int index = (int) (sequenceNumber / power);
            alienNumber.append(alienLanguage[index]);
            sequenceNumber -= index * power;
        }
        return alienNumber.toString().toCharArray();
    }

    /**
     * Base based logarithm of number
     *
     * @param number number
     * @param base   base
     * @return logarithm
     */
    private long log(long number, long base) {
        return (long) (Math.log(number) / Math.log(base));
    }
}