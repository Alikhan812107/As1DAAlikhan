import java.util.Random;

public class QuickSort {
    private static final Random RNG = new Random(0);

    public static void sort(int[] a, Metrics m, int cutoff) {
        quick(a, 0, a.length - 1, m, cutoff);
    }

    private static void quick(int[] a, int lo, int hi, Metrics m, int cutoff) {
        while (lo < hi) {
            if (hi - lo + 1 <= cutoff) { insertion(a, lo, hi, m); return; }

            int p = lo + RNG.nextInt(hi - lo + 1);
            swap(a, lo, p, m);
            int pivot = a[lo];
            int i = lo + 1, j = hi;

            while (true) {
                while (i <= hi) { m.incComp(); if (a[i] >= pivot) break; i++; }
                while (j >= lo + 1) { m.incComp(); if (a[j] <= pivot) break; j--; }
                if (i >= j) break;
                swap(a, i++, j--, m);
            }
            swap(a, lo, j, m);

            int leftSize = j - lo;
            int rightSize = hi - j;

            if (leftSize < rightSize) {
                m.enter(); try { quick(a, lo, j - 1, m, cutoff); } finally { m.exit(); }
                lo = j + 1;         // iterate on the larger side
            } else {
                m.enter(); try { quick(a, j + 1, hi, m, cutoff); } finally { m.exit(); }
                hi = j - 1;
            }
        }
    }

    private static void swap(int[] a, int i, int j, Metrics m) {
        int t = a[i]; a[i] = a[j]; a[j] = t; m.incSwap(); m.incWrite(2);
    }

    private static void insertion(int[] a, int lo, int hi, Metrics m) {
        for (int i = lo + 1; i <= hi; i++) {
            int v = a[i]; int j = i - 1;
            while (j >= lo) { m.incComp(); if (a[j] <= v) break; a[j + 1] = a[j--]; m.incWrite(1); }
            a[j + 1] = v; m.incWrite(1);
        }
    }
}
