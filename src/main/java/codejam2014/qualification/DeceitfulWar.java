package codejam2014.qualification;

import java.io.*;
import java.util.*;

public class DeceitfulWar {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2014/qualification";

    private static final String SAMPLE = "D-sample.in";
    private static final String SMALL = "D-small-practice.in";
    private static final String LARGE = "D-large-practice.in";

    private Scanner scanner;
    private PrintWriter writer;

    public DeceitfulWar(InputStream is, OutputStream os) {
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

        DeceitfulWar problem = new DeceitfulWar(is, os);
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

    private double[] naomi;
    private double[] ken;

    /**
     * Solve the problem
     */
    public void solve() {
        int t = scanner.nextInt();

        // 1 <= T <= 50
        for (int i = 1; i <= t; i++) {
            writer.print("Case #");
            writer.print(i + ": ");

            // 1 <= N <= 1000
            int n = scanner.nextInt();

            naomi = new double[n];
            for(int j=0; j<n; j++)
                naomi[j] = scanner.nextDouble();

            ken = new double[n];
            for(int j=0; j<n; j++)
                ken[j] = scanner.nextDouble();

            solveTest();
        }
    }

    private void solveTest() {
        Arrays.sort(naomi);
        Arrays.sort(ken);

        writer.printf("%1$d %2$d\n", deceitfulWar(), war());
    }

    private int war() {
        LinkedList<Double> kenList = asList(ken);

        int naomiScore = 0;
        for (double naomiBlock: naomi) {
            boolean kenHasBigger = false;
            Iterator<Double> j = kenList.iterator();
            while(j.hasNext()) {
                double kenBlock = j.next();
                if (kenBlock > naomiBlock) {
                    kenHasBigger = true;
                    j.remove();
                    break;
                }
            }
            if (!kenHasBigger) {
                kenList.removeFirst();
                naomiScore++;
            }
        }
        return naomiScore;
    }

    private int deceitfulWar() {
        LinkedList<Double> naomiList = asList(naomi);
        LinkedList<Double> kenList = asList(ken);

        int naomiScore = 0;
        while (!naomiList.isEmpty()) {
           if (naomiList.getFirst() < kenList.getFirst()) {
               naomiList.removeFirst();
               kenList.removeLast();
           } else {
               boolean naomiHasBigger = false;
               Iterator<Double> j = naomiList.iterator();
               while(j.hasNext()) {
                   double naomiBlock = j.next();
                   if (naomiBlock > kenList.getFirst()) {
                       naomiHasBigger = true;
                       j.remove();
                       kenList.removeFirst();
                       naomiScore++;
                       break;
                   }
               }
               if (!naomiHasBigger) {
                   naomiList.removeFirst();
                   kenList.removeFirst();
               }
           }
       }
        return naomiScore;
    }

    private LinkedList<Double> asList(double[] array) {
        // don't like to have ArrayList here
        LinkedList<Double> list = new LinkedList<Double>();
        for (double block: array)
            list.add(block);
        return list;
    }
}
