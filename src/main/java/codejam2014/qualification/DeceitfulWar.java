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
    private static final String LARGE = "D-large.in";

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
            // runTest(LARGE, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // problem part
    private Chest[] chests;

    /**
     * Solve the problem
     */
    public void solve() {
        int t = scanner.nextInt();

        // 1 <= T <= 25
        for (int i = 1; i <= t; i++) {
            writer.print("Case #");
            writer.print(i + ": ");

            // 1 <= K
            // 1 <= N <= 200
            int k = scanner.nextInt();
            int n = scanner.nextInt();

            int keys[] = new int[k];
            for (int j=0; j<k; j++)
                keys[j] = scanner.nextInt();

            chests = new Chest[n];
            for (int j=0; j<n; j++) {
                chests[j] = new Chest();
                chests[j].type = scanner.nextInt();

                int ki = scanner.nextInt();
                chests[j].keys = new int[ki];

                for (int l=0; l<ki; l++)
                    chests[j].keys[l] = scanner.nextInt();
            }
            solve(keys);
        }
    }

    private void solve(int keys[]) {
        Opening opening = new Opening(keys);
        if (!opening.solve())
            writer.println("IMPOSSIBLE");
    }

    private static class Chest {
        private int type;
        private int keys[];
    }

    private class Opening {
        private Map<Integer, Integer> keysMap;
        private Set<Integer> opened;

        private Opening(int keys[]) {
            keysMap = new HashMap<Integer, Integer>();
            opened = new LinkedHashSet<Integer>();

            for (int key: keys)
                addKey(key);
        }

        private Opening(Opening opening) {
            keysMap = new HashMap<Integer, Integer>(opening.keysMap);
            opened = new LinkedHashSet<Integer>(opening.opened);
        }

        // O(200^400) ?
        private boolean solve() {
            if (isAllOpened()) {
                printResult();
                return true;
            }

            for (int i=0; i<chests.length; i++) {
                if (canBeOpened(i)) {
                    Opening opening = open(i);
                    if (opening.solve())
                        return true;
                }
            }
            return false;
        }

        private boolean canBeOpened(int index) {
            if (opened.contains(index))
                return false;

            return getKeyCount(chests[index].type) > 0;
        }

        private Opening open(int index) {
            Opening opening = new Opening(this);

            opening.opened.add(index);
            opening.deleteKey(chests[index].type);
            for (int key: chests[index].keys)
                opening.addKey(key);

            return opening;
        }

        private boolean isAllOpened() {
            for (int i=0; i<chests.length; i++)
                if (!opened.contains(i))
                    return false;
            return true;
        }

        private void printResult() {
            for (int index: opened) {
                writer.print(index+1);
                writer.print(" ");
            }
            writer.println();
        }

        private int getKeyCount(int type) {
            Integer count = keysMap.get(type);
            if (count == null)
                return 0;
            return count;
        }

        private void addKey(int type) {
            Integer count = keysMap.get(type);
            if (count == null)
                count = 0;
            keysMap.put(type, ++count);
        }

        private void deleteKey(int type) {
            Integer count = keysMap.get(type);
            if (count == null)
                return;

            if (count > 1)
                keysMap.put(type, --count);
            else
                keysMap.remove(type);
        }
    }
}
