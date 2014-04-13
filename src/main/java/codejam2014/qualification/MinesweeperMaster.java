package codejam2014.qualification;

import java.io.*;
import java.util.*;

public class MinesweeperMaster {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2014/qualification";

    private static final String SAMPLE = "C-sample.in";
    private static final String SMALL = "C-small-attempt1.in";
    private static final String LARGE = "C-large.in";

    private Scanner scanner;
    private PrintWriter writer;

    public MinesweeperMaster(InputStream is, OutputStream os) {
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

        MinesweeperMaster problem = new MinesweeperMaster(is, os);
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

    private int r, c, m;

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

            // 1 <= R, C <= 50
            r = scanner.nextInt();
            c = scanner.nextInt();

            // 0 <= M < R * C
            m = scanner.nextInt();

            writer.print(brute(r, c, m));
       }
    }

    public String brute(int r, int c, int m) {
        Mine[] mines = new Mine[m];
        Mine mine = new Mine(0, 0);
        fill(mines, mine, 0);

        Minesweeper minesweeper = new Minesweeper(mines);
        while(true) {
            // System.out.println(minesweeper.toString());

            for (int i=0; i<r; i++)
                for (int j=0; j<c; j++) {
                    if (minesweeper.hasMine(i, j))
                        continue;

                    if (minesweeper.click(i, j))
                        return minesweeper.toString(i, j);
                }

            if (!minesweeper.hasNext())
                return "Impossible\n";

            minesweeper = minesweeper.next();
        }
    }

    private void fill(Mine[] mines, Mine mine, int k) {
        for(int i=k; i<m; i++) {
            mines[i] = mine;
            if (i != m-1)
                mine = mine.next();
        }
    }

    private class Mine {
        int i, j;

        private Mine(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public boolean hasNext() {
            return (i != r-1) || (j != c-1);
        }

        public Mine next() {
            if (!hasNext())
                throw new IllegalStateException();

            if (j+1 < c)
                return new Mine(i, j+1);
            return new Mine(i+1, 0);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Mine mine = (Mine) o;

            if (i != mine.i) return false;
            if (j != mine.j) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = i;
            result = 31 * result + j;
            return result;
        }

        @Override
        public String toString() {
            return "{" + i +"," + j + "}";
        }
    }

    private class Minesweeper {
        private Mine[] mines;
        private Set<Mine> mineSet;

        public Minesweeper(Mine[] mines) {
            this.mines = mines;
            this.mineSet = new HashSet<Mine>(Arrays.asList(mines));
        }

        public boolean hasNext() {
            int index = m-1;

            for(int i=r-1; i>=0; i--)
                for(int j=c-1; j>=0; j--) {
                    if (mines[index].equals(new Mine(i, j))) {
                        if (index == 0)
                            return false;
                        index--;
                    } else
                        return true;
                }
            return true;
        }

        public Minesweeper next() {
            int index = m-1;

            for(int i=r-1; i>=0; i--)
                for(int j=c-1; j>=0; j--) {
                    if (mines[index].equals(new Mine(i, j))) {
                        index--;
                    } else {
                        Mine[] copy = Arrays.copyOf(mines, mines.length);
                        fill(copy, copy[index].next(), index);
                        return new Minesweeper(copy);
                    }
                }
            throw new IllegalStateException();
        }

        public boolean hasMine(int i, int j) {
            return mineSet.contains(new Mine(i, j));
        }

        public boolean click(int i, int j) {
            boolean[][] visited = new boolean[r][c];
            for(int k=0; k<r; k++)
                for(int l=0; l<c; l++)
                    visited[k][l] = false;

            visit(i, j, visited);

            for(int k=0; k<r; k++)
                for(int l=0; l<c; l++)
                    if ((!visited[k][l]) && (!hasMine(k, l)))
                        return false;

            return true;
        }

        private void visit(int i, int j, boolean[][] visited) {
            if (visited[i][j])
                return;

            visited[i][j] = true;

            Iterable<Mine> neighbors = neighbors(i, j);
            if (count(neighbors) == 0) {
                for (Mine mine : neighbors)
                    visit(mine.i, mine.j, visited);
            }
        }

        private int count(Iterable<Mine> neighbors) {
            int mines = 0;
            for(Mine mine: neighbors)
                if (mineSet.contains(mine))
                    mines++;
            return mines;
        }

        public Iterable<Mine> neighbors(int i, int j) {
            List<Mine> mines = new LinkedList<Mine>();

            addIfOk(mines, i-1, j-1);
            addIfOk(mines, i-1, j);
            addIfOk(mines, i-1, j+1);
            addIfOk(mines, i, j-1);
            addIfOk(mines, i, j+1);
            addIfOk(mines, i+1, j-1);
            addIfOk(mines, i+1, j);
            addIfOk(mines, i+1, j+1);

            return mines;
        }

        private void addIfOk(List<Mine> mines, int i, int j) {
            if ((i >= 0) && (i < r) && (j >= 0) && (j < c))
                mines.add(new Mine(i, j));
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            for(int k=0; k<r; k++) {
                for (int l = 0; l < c; l++) {
                    if (hasMine(k, l))
                        sb.append("*");
                    else
                        sb.append(".");
                }
                sb.append("\n");
            }
            return sb.toString();
        }

        public String toString(int i, int j) {
            StringBuilder sb = new StringBuilder();

            for(int k=0; k<r; k++) {
                for (int l = 0; l < c; l++) {
                    if (hasMine(k, l))
                        sb.append("*");
                    else if ((i == k) && (j == l))
                        sb.append("c");
                    else
                        sb.append(".");
                }
                sb.append("\n");
            }
            return sb.toString();
        }
    }
}