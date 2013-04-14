package codejam.practice;

import java.io.*;
import java.util.Scanner;

public class AlwaysTurnLeft {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam/practice";

    private static final String SAMPLE = "B-sample-practice.in";
    private static final String SMALL = "B-small-practice.in";
    private static final String LARGE = "B-large-practice.in";

    private Scanner scanner;
    private PrintWriter writer;

    public AlwaysTurnLeft(InputStream is, OutputStream os) {
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

        AlwaysTurnLeft problem = new AlwaysTurnLeft(is, os);
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
    
    private static final int mazeSize = 2 * (int) Math.sqrt(10000);
    private MazeOptions maze[][];

    public void solve() {
        initMaze();

        int n = scanner.nextInt();
        for (int i = 1; i <= n; i++) {
            writer.print("Case #");
            writer.println(i + ": ");

            char[] entranceToExit = scanner.next().toCharArray();
            char[] exitToEntrance = scanner.next().toCharArray();

            cleanOptions();

            MazeCoordinates mc = new MazeCoordinates(0, 0, MazeDirection.SOUTH);
            mc = solveOneWay(mc, entranceToExit);

            MazeDirection exitMd = mc.getMd();

            mc.turnBack();
            mc = solveOneWay(mc, exitToEntrance);

            writer.print(getOptionsCode(mc, exitMd));
        }
    }

    private void initMaze() {
        maze = new MazeOptions[mazeSize][2 * mazeSize];

        for (int i = 0; i < maze.length; i++)
            for (int j = 0; j < maze[i].length; j++)
                maze[i][j] = new MazeOptions();
    }

    private MazeCoordinates solveOneWay(MazeCoordinates mc, char[] path) {
        for (int i = 0; i < path.length; i++) {
            switch (path[i]) {
                case 'W':
                    updateOptionsExit(mc);
                    mc.walk();
                    updateOptionsEnter(mc);
                    break;

                case 'L':
                    mc.turnLeft();
                    break;

                case 'R':
                    mc.turnRight();
                    break;
            }
        }
        return mc;
    }

    private String getOptionsCode(MazeCoordinates enterMc, MazeDirection exitMd) {
        int minY = 1;
        int maxY = enterMc.getMaxY();

        int minX = enterMc.getMinX();
        int maxX = enterMc.getMaxX();

        switch (exitMd) {
            case NORTH:
                // nothing, already 1
                break;

            case EAST:
                maxX--;
                break;

            case SOUTH:
                maxY--;
                break;

            case WEST:
                minX++;
                break;
        }
        return getOptionsCode(minY, maxY, minX, maxX);
    }

    private String getOptionsCode(int minY, int maxY, int minX, int maxX) {
        StringBuilder sb = new StringBuilder();
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                MazeOptions mo = getMazeOptions(x, y);
                sb.append(mo.toString());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void cleanOptions() {
        for (int i = 0; i < maze.length; i++)
            for (int j = 0; j < maze[i].length; j++)
                maze[i][j].clean();
    }

    private void updateOptionsExit(MazeCoordinates mc) {
        MazeOptions mo = getMazeOptions(mc.getX(), mc.getY());
        mo.canWalkOnExit(mc);
    }

    private void updateOptionsEnter(MazeCoordinates mc) {
        MazeOptions mo = getMazeOptions(mc.getX(), mc.getY());
        mo.canWalkOnEnter(mc);
    }

    private MazeOptions getMazeOptions(int x, int y) {
        return maze[y][mazeSize + x];
    }


    private class MazeOptions {
        private boolean canWalkNorth;
        private boolean canWalkSouth;
        private boolean canWalkWest;
        private boolean canWalkEast;

        public MazeOptions() {
            clean();
        }

        public void clean() {
            canWalkNorth = false;
            canWalkSouth = false;
            canWalkWest = false;
            canWalkEast = false;
        }

        public void canWalkOnExit(MazeCoordinates mc) {
            switch (mc.getMd()) {
                case NORTH:
                    canWalkNorth = true;
                    break;

                case EAST:
                    canWalkEast = true;
                    break;

                case SOUTH:
                    canWalkSouth = true;
                    break;

                case WEST:
                    canWalkWest = true;
                    break;
            }
        }

        public void canWalkOnEnter(MazeCoordinates mc) {
            switch (mc.getMd()) {
                case NORTH:
                    canWalkSouth = true;
                    break;

                case EAST:
                    canWalkWest = true;
                    break;

                case SOUTH:
                    canWalkNorth = true;
                    break;

                case WEST:
                    canWalkEast = true;
                    break;
            }
        }

        @Override
        public String toString() {
            int north = (canWalkNorth) ? 1 : 0;
            int south = (canWalkSouth) ? 2 : 0;
            int west = (canWalkWest) ? 4 : 0;
            int east = (canWalkEast) ? 8 : 0;

            int result = north | south | west | east;
            return Integer.toHexString(result);
        }
    }


    private class MazeCoordinates {
        private int x;
        private int y;

        private MazeDirection md;

        private int minX;
        private int maxX;

        private int minY;
        private int maxY;

        public MazeCoordinates(int x, int y, MazeDirection md) {
            this.x = x;
            this.y = y;
            this.md = md;

            minX = x;
            maxX = x;

            minY = y;
            maxY = y;
        }

        public MazeCoordinates(MazeCoordinates mc) {
            this.x = mc.x;
            this.y = mc.y;
            this.md = mc.md;

            this.minX = mc.minX;
            this.maxX = mc.maxX;

            this.minY = mc.minY;
            this.maxY = mc.maxY;
        }

        public void walk() {
            switch (md) {
                case NORTH:
                    y--;
                    break;

                case EAST:
                    x++;
                    break;

                case SOUTH:
                    y++;
                    break;

                case WEST:
                    x--;
                    break;

                default:
                    throw new IllegalStateException("Illegal walk");
            }
            updateMinMax();
        }

        private void updateMinMax() {
            if (minX > x)
                minX = x;
            if (maxX < x)
                maxX = x;
            if (minY > y)
                minY = y;
            if (maxY < y)
                maxY = y;
        }

        public void turnLeft() {
            md = md.turnLeft();
        }

        public void turnRight() {
            md = md.turnRight();
        }

        public void turnBack() {
            md = md.turnRight();
            md = md.turnRight();
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getMinX() {
            return minX;
        }

        public int getMaxX() {
            return maxX;
        }

        public int getMinY() {
            return minY;
        }

        public int getMaxY() {
            return maxY;
        }

        public MazeDirection getMd() {
            return md;
        }

        @Override
        public String toString() {
            return "x: " + x + ", y: " + y + ", " + md;
        }
    }


    private enum MazeDirection {
        NORTH, SOUTH, WEST, EAST;

        public MazeDirection turnLeft() {
            switch (this) {
                case NORTH:
                    return MazeDirection.WEST;

                case EAST:
                    return MazeDirection.NORTH;

                case SOUTH:
                    return MazeDirection.EAST;

                case WEST:
                    return MazeDirection.SOUTH;
            }
            throw new IllegalStateException("Illegal turn left");
        }

        public MazeDirection turnRight() {
            switch (this) {
                case NORTH:
                    return MazeDirection.EAST;

                case EAST:
                    return MazeDirection.SOUTH;

                case SOUTH:
                    return MazeDirection.WEST;

                case WEST:
                    return MazeDirection.NORTH;
            }
            throw new IllegalStateException("Illegal turn right");
        }
    }
}