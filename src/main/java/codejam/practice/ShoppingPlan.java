package codejam.practice;

import java.io.*;
import java.util.*;

public class ShoppingPlan {

    // general part
    private static final String INPUT = "src/main/resources";
    private static final String OUTPUT = "target/output";
    private static final String ROUND = "codejam/practice";

    private static final String SAMPLE = "D-sample-practice.in";
    private static final String SMALL = "D-small-practice.in";
    private static final String LARGE = "D-large-practice.in";

    private Scanner scanner;
    private PrintWriter writer;

    public ShoppingPlan(InputStream is, OutputStream os) {
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

        ShoppingPlan problem = new ShoppingPlan(is, os);
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
            //runTest(SMALL, false);
            //runTest(LARGE, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // problem part
    private static final int MAX_STORES = 50;
    private static final int MAX_ITEMS = 15;

    private Map<String, Item> itemsMap;
    private Store[] stores;
    private int priceOfGas;

    /**
     * Solve the problem
     */
    public void solve() {
        int n = scanner.nextInt();

        for (int i = 1; i <= n; i++) {
            writer.print("Case #");
            writer.print(i + ": ");

            int numItems = scanner.nextInt();
            int numStores = scanner.nextInt();
            priceOfGas = scanner.nextInt();

            itemsMap = new HashMap<String, Item>();
            for (int j = 0; j < numItems; j++) {
                Item item = new Item(scanner.next());
                itemsMap.put(item.getName(), item);
            }

            stores = new Store[numStores];
            for (int j = 0; j < numStores; j++)
                stores[j] = new Store(scanner.nextInt(), scanner.nextInt(), scanner.nextLine().trim());

            writer.printf("%1$.7f\n", branchAndBound());
        }
    }

    private double branchAndBound() {
        double bound = Double.MAX_VALUE;

        for (int i = 0; i < stores.length; i++) {
            Queue<Integer> visited = new LinkedList<Integer>();
            visited.add(i);
            bound = bound = Math.min(bound, branchAndBound(i, new Purchase(stores[i]), visited, bound, ""));
            visited.remove(i);
        }
        return bound;
    }

    private double branchAndBound(int current, Purchase purchase, Queue<Integer> visited, double bound, String tab) {
        System.out.printf("%1$s %2$d, %3$s, %4$f.7\n", tab, current, visited, bound);

        if (visited.size() == stores.length)
            bound = (purchase.allBought) ? purchase.totalCost : bound;
        else {
            for (int i = 0; i < stores.length; i++) {
                if (visited.contains(i))
                    continue;

                Purchase next = new Purchase(stores[i], purchase, stores[current].getDistance(stores[i]));
                visited.add(i);
                bound = Math.min(bound, branchAndBound(i, next, visited, bound, tab + "\t"));
                visited.remove();

                if ((next.allBought) && (next.totalCost < bound))
                    bound = next.totalCost;
            }
        }

        System.out.printf("%1$s %2$d, %3$s --> %4$f.7\n", tab, current, visited, bound);
        return bound;
    }

    public double dijkstra() {
        double minCost = Double.MAX_VALUE;
        Queue<Store> queue = new LinkedList<Store>();

        Map<Store, Purchase> purchases = new HashMap<Store, Purchase>();
        for (Store store : stores) {
            queue.add(store);
            purchases.put(store, new Purchase(store));
        }

        while (queue.size() > 0) {
            double findCost = Double.MAX_VALUE;
            Store findStore = null;
            for (Store store : queue) {
                if ((!purchases.get(store).isPerishable()) && (findCost > purchases.get(store).getFutureCost())) {
                    findCost = purchases.get(store).getFutureCost();
                    findStore = store;
                }
            }
            if (findStore == null) {
                findCost = Double.MAX_VALUE;
                findStore = queue.peek();
                for (Store store : queue) {
                    if (findCost > purchases.get(store).getFutureCost()) {
                        findCost = purchases.get(store).getFutureCost();
                        findStore = store;
                    }
                }
            }
            Store current = findStore;
            queue.remove(findStore);

            for (Store store : stores) {
                if (store == current)
                    continue;

                Purchase purchase = purchases.get(current);
                Purchase next = new Purchase(store, purchase, current.getDistance(store));

                Purchase prev = purchases.get(store);
                if (prev.isAllBought()) {
                    if (prev.totalCost > next.totalCost)
                        purchases.put(store, next);
                } else
                    purchases.put(store, next);

                if (purchases.get(store).allBought)
                    minCost = Math.min(minCost, purchases.get(store).totalCost);
            }
        }
        return minCost;
    }

    private double getMinCost() {
        double minCost = Double.MAX_VALUE;

        Purchase[][] purchases = new Purchase[stores.length][stores.length];

        for (int i = 0; i < stores.length; i++) {
            Purchase prev = null;
            for (int j = 0; j < stores.length; j++) {
                if (i == j)
                    continue;

                if (prev == null)
                    purchases[i][j] = new Purchase(stores[j]);
                else
                    purchases[i][j] = new Purchase(stores[j], prev, stores[i].getDistance(stores[j]));

                prev = purchases[i][j];

                if (purchases[i][j].allBought)
                    minCost = Math.min(minCost, purchases[i][j].totalCost);
            }
        }
        return minCost;
    }

    private class Item {
        public Item(String string) {
            if (string.endsWith("!")) {
                perishable = true;
                name = string.substring(0, string.length() - 1);
            } else {
                perishable = false;
                name = string;
            }
        }

        private String name;
        private boolean perishable;

        public String getName() {
            return name;
        }

        public boolean isPerishable() {
            return perishable;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object obj) {
            return name.equals(obj);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    private class Store {
        private int x;
        private int y;
        private Map<Item, Integer> itemPriceMap;

        public Store(int x, int y, String prices) {
            this.x = x;
            this.y = y;

            itemPriceMap = new HashMap<Item, Integer>();

            for (String string : prices.split(" ")) {
                String[] array = string.split(":");
                itemPriceMap.put(itemsMap.get(array[0]), Integer.parseInt(array[1]));
            }
        }

        public double getDistance() {
            return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        }

        public double getDistance(Store store) {
            return Math.sqrt(Math.pow(x - store.x, 2) + Math.pow(y - store.y, 2));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Store store = (Store) o;

            if (x != store.x) return false;
            if (y != store.y) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Integer getPrice(Item item) {
            return itemPriceMap.get(item);
        }
    }

    private class Purchase implements Comparable {
        private Store store;
        private double distance;
        private Map<Item, Integer> itemsPurchaseMap;
        private boolean perishable;
        private boolean allBought;
        private double totalCost;

        private Purchase(Store store, double distance) {
            this.store = store;
            this.distance = distance;
            itemsPurchaseMap = new HashMap<Item, Integer>();
            perishable = false;
        }

        public Purchase(Store store) {
            this(store, store.getDistance());

            for (Item item : itemsMap.values())
                initItemPurchase(store, item);

            updateAllBought(store);
        }

        //TODO: fix me

        public Purchase(Store store, Purchase purchase, double distance) {
            this(store, purchase.distance + distance);

            for (Item item : itemsMap.values()) {
                initItemPurchase(store, item);

                if (purchase.itemsPurchaseMap.containsKey(item)) {
                    if (itemsPurchaseMap.containsKey(item))
                        itemsPurchaseMap.put(item,
                                Math.min(itemsPurchaseMap.get(item), purchase.itemsPurchaseMap.get(item)));
                    else
                        itemsPurchaseMap.put(item, purchase.itemsPurchaseMap.get(item));
                }
            }
            updateAllBought(store);
        }


        private void updateAllBought(Store store) {
            allBought = true;
            totalCost = (store.getDistance() + distance) * priceOfGas;
            for (Item item : itemsMap.values()) {
                if (itemsPurchaseMap.containsKey(item))
                    totalCost += itemsPurchaseMap.get(item);
                else
                    allBought = false;
            }
        }

        private void initItemPurchase(Store store, Item item) {
            if (store.getPrice(item) != null) {
                itemsPurchaseMap.put(item, store.getPrice(item));
                perishable = perishable || item.isPerishable();
            }
        }

        public Store getStore() {
            return store;
        }

        public double getDistance() {
            return distance;
        }

        public boolean isPerishable() {
            return perishable;
        }

        public boolean isAllBought() {
            return allBought;
        }

        public double getTotalCost() {
            // return (allBought) ? totalCost : Double.MAX_VALUE;
            return totalCost;
        }

        public double getFutureCost() {
            if (!perishable)
                return totalCost;
            return (allBought) ? totalCost : totalCost + 2 * store.getDistance();
        }

        public int compareTo(Object o) {
            return ((Double) totalCost).compareTo(((Purchase) o).getTotalCost());
        }
    }
}