package codejam2010.qualification;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class ThemePark {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2010/qualification";

    private static final String SAMPLE = "C-sample.in";
    private static final String SMALL = "C-small-attempt0.in";
    private static final String LARGE = "C-large.in";

    private Scanner scanner;
    private PrintWriter writer;

    public ThemePark(InputStream is, OutputStream os) {
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

        ThemePark problem = new ThemePark(is, os);
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

            // 1 <= R <= 10^8
            long r = scanner.nextInt();

            // 1 <= k <= 10^9
            long k = scanner.nextInt();

            // 1 <= N <= 1000
            int n = scanner.nextInt();

            // 1 <= g[i] <= 10^7
            long g[] = new long[n];
            for (int j = 0; j < n; j++)
                g[j] = scanner.nextInt();

            writer.println(solve(r, k, g));
        }
    }

    private String solve(long r, long k, long g[]) {
        Queue<Long> queue = new LinkedList<Long>();
        Queue<Long> ride = new LinkedList<Long>();

        // queue, ride -> count, euros
        Map<Key, Value> map = new LinkedHashMap<Key, Value>();

        for (int i = 0; i < g.length; i++)
            queue.add(g[i]);

        BigInteger euros = new BigInteger("0");
        for (int i = 0; i < r; i++) {
            long sum = 0;
            ride.clear();

            while ((queue.size() > 0) && (sum + queue.peek() <= k)) {
                ride.add(queue.peek());
                sum += queue.poll();
            }

            // prepare key->value pair for caching...
            Key key = new Key(queue.toString(), ride.toString());
            Value value = new Value(ride.size(), sum);

            if (!map.containsKey(key)) {
                map.put(key, value);

                euros = euros.add(new BigInteger(((Long) sum).toString()));
                queue.addAll(ride);
            } else {
                // the same [ride, queue] found in cache  
                // so calculate euros based on cache
                euros = euros.add(calculateByMap(map, key, r - i));
                break;
            }
        }
        return euros.toString();
    }

    private BigInteger calculateByMap(Map<Key, Value> map, Key key, long ridesLeft) {
        long storedSum = 0;
        boolean found = false;
        int foundIndex = 0;

        long remainder = 0;
        long remainderSum = 0;

        for (Key storedKey : map.keySet()) {
            // if item found - calculate remainder
            if ((!found) && (storedKey.equals(key))) {
                found = true;
                remainder = ridesLeft % (map.size() - foundIndex);
            }
            if (found) {
                // calculate stored sum
                storedSum += map.get(storedKey).getSum();

                // calculate remainder
                if (remainder > 0) {
                    remainderSum += map.get(storedKey).getSum();
                    remainder--;
                }
            } else
                foundIndex++;
        }
        long storedCount = map.size() - foundIndex;
        long mult = ridesLeft / (storedCount);
        mult = mult * storedSum + remainderSum;
        return new BigInteger(((Long) mult).toString());
    }

    class Key {
        private String queue;
        private String ride;

        Key(String queue, String ride) {
            this.queue = queue;
            this.ride = ride;
        }

        public String getQueue() {
            return queue;
        }

        public String getRide() {
            return ride;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (!queue.equals(key.queue)) return false;
            if (!ride.equals(key.ride)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = queue.hashCode();
            result = 31 * result + ride.hashCode();
            return result;
        }
    }

    class Value {
        private int size;
        private long sum;

        Value(int size, long sum) {
            this.size = size;
            this.sum = sum;
        }

        public int getSize() {
            return size;
        }

        public long getSum() {
            return sum;
        }
    }
}