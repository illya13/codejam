package codejam2013.qualification;

import java.io.*;
import java.util.*;

public class TicTacToeTomek {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2013/qualification";

    private static final String SAMPLE = "A-sample.in";
    private static final String SMALL = "A-small-attempt0.in";
    private static final String LARGE = "A-large.in";

    private Scanner scanner;
    private PrintWriter writer;

    public TicTacToeTomek(InputStream is, OutputStream os) {
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

        TicTacToeTomek problem = new TicTacToeTomek(is, os);
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
        Board.Builder builder = new Board.Builder();

        // 1 <= T <= 1000
        int t = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= t; i++) {
            writer.print("Case #");
            writer.print(i + ": ");

            builder.newBoard();
            for (int j = 0; j < 4; j++) {
                String line = scanner.nextLine();
                builder.addLine(line);
            }
            scanner.nextLine();

            Board board = builder.build();
            writer.println(board.gameState());
        }
    }

    private static class Board {
        private static Map<Board, String> cache = new HashMap<Board, String>(1000);

        private char[][] symbols;
        private int hash = 0;

        @Override
        public int hashCode() {
            if (hash != 0)
                return hash;
            for (int i=0; i<4; i++)
                for (int j=0; j<4; j++)
                    hash = 31*hash + symbols[i][j];
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;

            if (obj instanceof Board) {
                Board board = (Board) obj;
                for (int i=0; i<4; i++)
                    for (int j=0; j<4; j++)
                        if (symbols[i][j] != board.symbols[i][j])
                            return false;
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<4; i++) {
                for (int j=0; j<4; j++)
                    sb.append(symbols[i][j]);
                sb.append('\n');
            }
            return sb.toString();
        }

        private String gameState() {
            if (!cache.containsKey(this))
                cache.put(this, evaluateGameState());
            return cache.get(this);
        }

        // O(C*4*4), C < 16
        private String evaluateGameState() {
            if (evaluatePlayer('X'))
                return "X won";

            if (evaluatePlayer('O'))
                return "O won";

            if (isCompleted())
                return "Draw";
            else
                return "Game has not completed";
        }

        private boolean evaluatePlayer(char player) {
            for (int i=0; i<4; i++) {
                if (evaluateRow(player, i))
                    return true;
            }

            for (int i=0; i<4; i++) {
                if (evaluateColumn(player, i))
                    return true;
            }

            return evaluateDiag1(player) || evaluateDiag2(player);
        }

        private boolean evaluateRow(char player, int row) {
            for (int i=0; i<4; i++)
                if ((symbols[row][i] != 'T') && (symbols[row][i] != player))
                    return false;
            return true;
        }

        private boolean evaluateColumn(char player, int column) {
            for (int i=0; i<4; i++)
                if ((symbols[i][column] != 'T') && (symbols[i][column] != player))
                    return false;
            return true;
        }

        private boolean evaluateDiag1(char player) {
            for (int i=0; i<4; i++)
                if ((symbols[i][i] != 'T') && (symbols[i][i] != player))
                    return false;
            return true;
        }

        private boolean evaluateDiag2(char player) {
            for (int i=0; i<4; i++)
                if ((symbols[3-i][i] != 'T') && (symbols[3-i][i] != player))
                    return false;
            return true;
        }

        private boolean isCompleted() {
            for (int i=0; i<4; i++)
                for (int j=0; j<4; j++)
                    if (symbols[i][j] == '.')
                        return false;
            return true;
        }

        private static class Builder {
            private Board board;
            private int lines;

            private Builder() {
            }

            private Builder newBoard() {
                board = new Board();
                board.symbols = new char[4][4];
                lines = 0;
                return this;
            }

            private Builder addLine(String line) {
                for (int i=0; i<4; i++)
                    board.symbols[lines][i] = line.charAt(i);
                lines++;
                return this;
            }

            private Board build() {
                return board;
            }
        }
    }
}