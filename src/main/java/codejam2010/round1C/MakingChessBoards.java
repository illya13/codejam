package codejam2010.round1C;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class MakingChessBoards {
    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2010/round1C";

    private static final String SAMPLE = "C-sample.in";
    private static final String SMALL = "C-small-attempt0.in";
    private static final String LARGE = "C-large.in";
    

    Scanner scanner;
    PrintWriter pw;


    public void open(String name) throws FileNotFoundException {
        scanner = new Scanner(new File(INPUT + File.separator + ROUND + File.separator + LARGE));
        pw = new PrintWriter(name + ".txt");
    }

    public void run() {
        long totalN = scanner.nextLong();
        long caseN = 0;
        while (totalN-- > 0) {
            pw.print("Case #" + (++caseN) + ": ");
            solve();
            pw.flush();
        }
    }

    private void solve() {
        int m = scanner.nextInt();
        int n = scanner.nextInt();

        scanner.nextLine();

        int board[][] = new int[m][n];

        for (int i = 0; i < m; i++) {
            String line = scanner.nextLine();
            for (int j = 0; j < n / 4; j++) {
                int val = Integer.parseInt("" + line.charAt(j), 16);
                board[i][j * 4] = (val & (1 << 3)) > 0 ? 1 : 0;
                board[i][j * 4 + 1] = (val & (1 << 2)) > 0 ? 1 : 0;
                board[i][j * 4 + 2] = (val & (1 << 1)) > 0 ? 1 : 0;
                board[i][j * 4 + 3] = (val & 1) > 0 ? 1 : 0;
            }
        }
      //  dump(m, n, board);

        // size - count map
        SortedMap<Integer, Integer> sizeCount = new TreeMap<Integer, Integer>(new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });

        int maxSize = 0;

        do {
            int x = 0;
            int y = 0;
            maxSize = 0;

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    int size = findSubBoard(board, i, j);
                    if (size > maxSize) {
                        maxSize = size;
                        y = i;
                        x = j;
                    }
                }
            }

            if (maxSize == 0) {
                break;
            }
            Integer v = sizeCount.get(maxSize);
            if (v == null) {
                sizeCount.put(maxSize, 1);
            } else {
                sizeCount.put(maxSize, v + 1);
            }

            //  maxSize = findSubBoard(board, 0, 13);
            // System.out.println("MaxSize:" + maxSize + " x=" + x + "; y=" + y);

            for (int i = y; i < y + maxSize; i++) {
                for (int j = x; j < x + maxSize; j++) {
                    board[i][j] = 2;
                }
            }
            // dump(m, n, board);


        } while (maxSize > 0);

        pw.println(sizeCount.size());
        for (Map.Entry<Integer, Integer> entry : sizeCount.entrySet()) {
            pw.println(entry.getKey() + " " + entry.getValue());

        }


      //  System.out.println("ALL:" + sizeCount);
    }

    private void dump(int m, int n, int[][] board) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private int findSubBoard(int[][] board, int yy, int xx) {
        int start = board[yy][xx];

        if (!(start == 1 || start == 0)) {
            return 0;
        }

        int maxSize = Math.min(board[yy].length - xx, board.length - yy);

        int k = 1;
        _out:
        while (true) {
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < k; j++) {
                    if ((i + j) % 2 == 0) {
                        if (board[yy + i][xx + j] != start) {
                            k--;
                            break _out;
                        }
                    }
                    if ((i + j) % 2 == 1) {
                        if (board[yy + i][xx + j] == start || board[yy + i][xx + j] == 2) {
                            k--;
                            break _out;
                        }
                    }
                }
            }

            if (k == maxSize) {
                break;
            } else {
                k++;
            }
        }
        return k;
    }

    public void close() {
        pw.close();
        scanner.close();
    }

    public static void main(String[] args) {
        MakingChessBoards problem = new MakingChessBoards();
        try {
            problem.open("C-small-attempt0");
        } catch (FileNotFoundException fnfe) {
            return;
        }
        problem.run();
        problem.close();
    }
}