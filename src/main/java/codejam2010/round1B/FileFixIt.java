package codejam2010.round1B;

import java.io.*;
import java.util.*;

public class FileFixIt {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2010/round1B";

    private static final String SAMPLE = "A-sample.in";
    private static final String SMALL = "A-small-attempt0.in";
    private static final String LARGE = "A-large.in";

    private Scanner scanner;
    private PrintWriter writer;

    public FileFixIt(InputStream is, OutputStream os) {
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

        FileFixIt problem = new FileFixIt(is, os);
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

            // number of existing dirs, 0 <= N <= 100
            // !!! could be 0
            int n = scanner.nextInt();

            // number of dirs to create, 1 <= M <= 100
            int m = scanner.nextInt();

            Tree root = new Tree("");
            for (int j=0; j<n; j++) {
                String existing = scanner.next();
                String[] array = existing.split("/");
                Tree current = root;
                for(int k=1; k<array.length; k++) {
                    if (!current.dirs.containsKey(array[k]))
                        current.dirs.put(array[k], new Tree(array[k]));
                    current = current.dirs.get(array[k]);
                }
            }

            String create[] = new String[m];
            for (int j=0; j<m; j++)
                create[j] = scanner.next();

            writer.println(solve(root, create));
        }
    }

    private long solve(Tree root, String create[]) {
        long result = 0;
        for (int i=0; i<create.length; i++) {
            String[] array = create[i].split("/");
            Tree current = root;
            for(int j=1; j<array.length; j++) {
                if (!current.dirs.containsKey(array[j])) {
                    current.dirs.put(array[j], new Tree(array[j]));
                    result++;
                }
                current = current.dirs.get(array[j]);
            }
        }
        return result;
    }

    private class Tree {
        public String name;
        Map<String, Tree> dirs = new HashMap<String, Tree>();

        public Tree(String name) {
            this.name = name;
        }
    }
}