import java.util.*;
import java.util.stream.IntStream;

public class App {
    public static void main(String[] args) {
        Map<String,String> a = parse(args);
        String algo  = a.getOrDefault("algo", "quicksort");  // mergesort|quicksort|select
        int n        = Integer.parseInt(a.getOrDefault("n", "100000"));
        int trials   = Integer.parseInt(a.getOrDefault("trials", "3"));
        int cutoff   = Integer.parseInt(a.getOrDefault("cutoff", "16"));
        long seed    = Long.parseLong(a.getOrDefault("seed", "42"));

        System.out.println("algo,n,time_ms,depth,comps,swaps,writes,allocs");
        Random rnd = new Random(seed);

        for (int t = 0; t < trials; t++) {
            Metrics m = new Metrics();
            long start = System.nanoTime();

            switch (algo) {
                case "mergesort" -> {
                    int[] arr = rnd.ints(n, -1_000_000, 1_000_000).toArray();
                    MergeSort.sort(arr, m, cutoff);
                }
                case "quicksort" -> {
                    int[] arr = rnd.ints(n, -1_000_000, 1_000_000).toArray();
                    QuickSort.sort(arr, m, cutoff);
                }
                case "select" -> {
                    int[] arr = rnd.ints(n, -1_000_000, 1_000_000).toArray();
                    int k = rnd.nextInt(n);
                    int v = MoM5.select(arr, k, m);
                    if (v == Integer.MIN_VALUE + 1) System.err.print(""); // keep compiler honest
                }
                default -> { /* extend here for closest pair CLI if you like */ }
            }

            long ms = (System.nanoTime() - start) / 1_000_000;
            System.out.printf("%s,%d,%d,%d,%d,%d,%d,%d%n",
                    algo, n, ms, m.maxDepth, m.comps, m.swaps, m.writes, m.allocs);
        }
    }

    private static Map<String,String> parse(String[] args) {
        Map<String,String> m = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--")) {
                String k = args[i].substring(2);
                String v = (i + 1 < args.length && !args[i + 1].startsWith("--")) ? args[++i] : "true";
                m.put(k, v);
            }
        }
        return m;
    }
}
