package codejam2012.qualification;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SpeakingInTongues {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam2012/qualification";

    private static final String SAMPLE = "A-sample.in";
    private static final String SMALL = "A-small-attempt2.in";
    private static final String LARGE = "A-large.in";

    private Scanner scanner;
    private PrintWriter writer;

    public SpeakingInTongues(InputStream is, OutputStream os) {
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

        SpeakingInTongues problem = new SpeakingInTongues(is, os);
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
            //runTest(LARGE, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // problem part

    private static final String[] trainInput = new String [] {
            "y qee",
            "ejp mysljylc kd kxveddknmc re jsicpdrysi",
            "rbcpc ypc rtcsra dkh wyfrepkym veddknkmkrkcd",
            "de kr kd eoya kw aej tysr re ujdr lkgc jv"
    };

    private static final String[] trainOutput = new String [] {
            "a zoo",
            "our language is impossible to understand",
            "there are twenty six factorial possibilities",
            "so it is okay if you want to just give up"
    };

    private Map<Character, Character> map = new HashMap<Character, Character>();

    /**
     * Solve the problem
     */
    public void solve() {
        train();

        int t = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= t; i++) {
            writer.print("Case #");
            writer.print(i + ": ");

            String line = scanner.nextLine();
            writer.println(translate(line));
        }
    }

    private void train() {
        for (int i=0; i<trainInput.length; i++) {
            for(int j=0; j<trainInput[i].length(); j++) {
                map.put(trainInput[i].charAt(j), trainOutput[i].charAt(j));
            }
        }
        Character missingInput = null;
        Character missingOutput = null;
        for(char ch='a'; ch<='z'; ch++){
            if (!map.containsKey(ch))
                missingInput = ch;
            if (!map.containsValue(ch))
                missingOutput = ch;
        }
        if (missingInput != null)
            map.put(missingInput, missingOutput);
    }

    private String translate(String line) {
        StringBuilder sb = new StringBuilder();
        for(int j=0; j<line.length(); j++) {
            sb.append(map.get(line.charAt(j)));
        }
        return sb.toString();
    }
}