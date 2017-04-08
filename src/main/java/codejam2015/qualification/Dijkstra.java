package codejam2015.qualification;

import java.io.*;
import java.util.*;

public class Dijkstra {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2015/qualification";

    private static final String SAMPLE = "C-sample.in";
    private static final String SMALL = "C-small-practice.in";
    private static final String LARGE = "C-large.in";

    private Scanner scanner;
    private PrintWriter writer;

    public Dijkstra(InputStream is, OutputStream os) {
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

        Dijkstra problem = new Dijkstra(is, os);
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
//            runTest(LARGE, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // problem part

    /**
     * Solve the problem
     */
    public void solve() {
        // 1 <= T <= 230
        int t = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= t; i++) {
            writer.print("Case #");
            writer.println(i + ": ");

            // 1 <= L <= 10000.
            // 1 <= X <= 10^12.
            // 1 <= L * X <= 10^16.

            int l = scanner.nextInt();
            long x = scanner.nextLong();

            String chars = scanner.next();

            writer.printf("%1s\n", greedy(l, x, chars));
       }
    }

    private String greedy(int l, long x, String string) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<x; i++)
            sb.append(string);

        char[] chars = sb.toString().toCharArray();
        // Quaternion[][][] dp = new Quaternion[chars.length][chars.length][3];

        for(int i=1; i<chars.length-1; i++) {
            Quaternion dp1 = calc(chars, 0, i);
            Quaternion dp2 = Quaternion.one;
            Quaternion dp3 = calc(chars, i, chars.length);
            for (int j=i+1; j<=chars.length - 1; j++) {
/*
                dp[i-1][j][0] = calc(chars, 0, i);
                dp[i-1][j][1] = calc(chars, i, j);
                dp[i-1][j][2] = calc(chars, j, chars.length);
*/

                dp2 = dp2.mult(new Quaternion(chars[j-1]));
                dp3 = dp3.div(new Quaternion(chars[j-1]));

                if (Quaternion.i.equals(dp1) &&
                        Quaternion.j.equals(dp2) &&
                        Quaternion.k.equals(dp3) ) {
                    return "YES";
                }
            }
        }

        return "NO";
    }

    private Quaternion calc(char[] chars, int a, int b) {
        Quaternion quaternion = new Quaternion(chars[a]);
        for (int i=a+1; i<b; i++)
            quaternion = quaternion.mult(new Quaternion(chars[i]));
        return quaternion;
    }

    private enum QChar {
        ONE, I, J, K;

        @Override
        public String toString() {
            if (this.name().equals("ONE"))
                return "1";
            return this.name().toLowerCase();
        }
    }

    private static class Quaternion {
        private QChar qChar;
        private boolean sign;

        public static final Quaternion i = new Quaternion(QChar.I);
        public static final Quaternion j = new Quaternion(QChar.J);
        public static final Quaternion k = new Quaternion(QChar.K);
        public static final Quaternion one = new Quaternion(QChar.ONE);

        private static final Map<QuaternionPair, Quaternion> divMap = new HashMap<QuaternionPair, Quaternion>();
        static {
            for (QChar a: QChar.values()) {
                for (boolean signA: new boolean[]{false, true}) {
                    for (QChar b: QChar.values()) {
                        for (boolean signB: new boolean[]{false, true}) {
                            Quaternion qA = new Quaternion(a, signA);
                            Quaternion qB = new Quaternion(b, signB);
                            divMap.put(new QuaternionPair(qA.mult(qB), qA), qB);
                        }
                    }
                }
            }
        }


        public Quaternion(QChar qChar, boolean sign) {
            this.qChar = qChar;
            this.sign = sign;
        }

        public Quaternion(QChar qChar) {
            this(qChar, false);
        }

        public Quaternion(String ch) {
            this(QChar.valueOf(ch.toUpperCase()), false);
        }

        public Quaternion(char ch) {
            this(QChar.valueOf(new String(new char[] {Character.toUpperCase(ch)})), false);
        }

        public static Quaternion neg(Quaternion quaternion) {
            return new Quaternion(quaternion.qChar, !quaternion.sign);
        }

        public Quaternion mult(Quaternion quaternion) {
            Quaternion result = multUnsigned(quaternion);
            result.sign ^= sign ^ quaternion.sign;
            return result;
        }

        public Quaternion div(Quaternion quaternion) {
            return divMap.get(new QuaternionPair(this, quaternion));
        }

        private Quaternion multUnsigned(Quaternion quaternion) {
            switch (qChar) {
                case ONE:
                    return new Quaternion(quaternion.qChar);
                case I:
                    return multUnsignedI(quaternion);
                case J:
                    return multUnsignedJ(quaternion);
                case K:
                    return multUnsignedK(quaternion);
            }
            throw new IllegalArgumentException();
        }

        private Quaternion multUnsignedI(Quaternion quaternion) {
            switch (quaternion.qChar) {
                case ONE:
                    return new Quaternion(QChar.I);
                case I:
                    return new Quaternion(QChar.ONE, true);
                case J:
                    return new Quaternion(QChar.K);
                case K:
                    return new Quaternion(QChar.J, true);
            }
            throw new IllegalArgumentException();
        }

        private Quaternion multUnsignedJ(Quaternion quaternion) {
            switch (quaternion.qChar) {
                case ONE:
                    return new Quaternion(QChar.J);
                case I:
                    return new Quaternion(QChar.K, true);
                case J:
                    return new Quaternion(QChar.ONE, true);
                case K:
                    return new Quaternion(QChar.I);
            }
            throw new IllegalArgumentException();
        }

        private Quaternion multUnsignedK(Quaternion quaternion) {
            switch (quaternion.qChar) {
                case ONE:
                    return new Quaternion(QChar.K);
                case I:
                    return new Quaternion(QChar.J);
                case J:
                    return new Quaternion(QChar.I, true);
                case K:
                    return new Quaternion(QChar.ONE, true);
            }
            throw new IllegalArgumentException();
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Quaternion)) return false;

            Quaternion that = (Quaternion) o;

            if (sign != that.sign) return false;
            return qChar == that.qChar;

        }

        @Override
        public int hashCode() {
            int result = qChar.hashCode();
            result = 31 * result + (sign ? 1 : 0);
            return result;
        }

        @Override
        public String toString() {
            if (sign)
                return "-" + qChar.toString();

            return qChar.toString();
        }
    }

    private static class QuaternionPair {
        private final Quaternion a;
        private final Quaternion b;

        public QuaternionPair(Quaternion a, Quaternion b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof QuaternionPair)) return false;

            QuaternionPair that = (QuaternionPair) o;

            if (!a.equals(that.a)) return false;
            return b.equals(that.b);

        }

        @Override
        public int hashCode() {
            int result = a.hashCode();
            result = 31 * result + b.hashCode();
            return result;
        }
    }
}