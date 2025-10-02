import java.util.Arrays;

public class MoM5 {
    public static int select(int[] a, int k, Metrics m) {
        if (k < 0 || k >= a.length) throw new IllegalArgumentException("k out of range");
        return select(a, 0, a.length - 1, k, m);
    }

    private static int select(int[] a, int left, int right, int k, Metrics m) {
        while (true) {
            if (left == right) return a[left];
            if (right - left + 1 <= 32) { insertion(a, left, right, m); return a[k]; }

            int pivotIndex = medianOfMedians(a, left, right, m);
            pivotIndex = partition(a, left, right, pivotIndex, m);

            if (k == pivotIndex) return a[k];
            else if (k < pivotIndex) right = pivotIndex - 1;
            else left = pivotIndex + 1;
        }
    }

    // Recursively compress to medians-of-5 until <=5 remains, then return the median index
    private static int medianOfMedians(int[] a, int left, int right, Metrics m) {
        int n = right - left + 1;
        if (n <= 5) { insertion(a, left, right, m); return left + n / 2; }

        int numMeds = 0;
        for (int i = left; i <= right; i += 5) {
            int subR = Math.min(i + 4, right);
            insertion(a, i, subR, m);
            int median = i + (subR - i) / 2;
            swap(a, left + numMeds, median, m);
            numMeds++;
        }
        // find median of the [left .. left+numMeds-1] block (recurse)
        return medianOfMedians(a, left, left + numMeds - 1, m);
    }

    private static int partition(int[] a, int left, int right, int pivotIndex, Metrics m) {
        int pivot = a[pivotIndex];
        swap(a, pivotIndex, right, m);
        int store = left;
        for (int i = left; i < right; i++) {
            m.incComp();
            if (a[i] < pivot) { swap(a, store, i, m); store++; }
        }
        swap(a, store, right, m);
        return store;
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
