import java.util.Arrays;


public class MergeSort {
    public static void sort(int[] a, Metrics m, int cutoff) {
        if (a.length <= 1) return;
        int[] aux = new int[a.length];
        m.addAlloc((long) a.length * Integer.BYTES);
        sort(a, aux, 0, a.length - 1, cutoff, m);
    }

    private static void sort(int[] a, int[] aux, int lo, int hi, int cutoff, Metrics m) {
        m.enter();
        try {
            if (hi - lo + 1 <= cutoff) { insertion(a, lo, hi, m); return; }
            int mid = (lo + hi) >>> 1;
            sort(a, aux, lo, mid, cutoff, m);
            sort(a, aux, mid + 1, hi, cutoff, m);
            if (a[mid] <= a[mid + 1]) { m.incComp(); return; } // already merged
            merge(a, aux, lo, mid, hi, m);
        } finally { m.exit(); }
    }

    private static void merge(int[] a, int[] aux, int lo, int mid, int hi, Metrics m) {
        System.arraycopy(a, lo, aux, lo, hi - lo + 1);
        m.incWrite(hi - lo + 1);

        int i = lo, j = mid + 1, k = lo;
        while (i <= mid && j <= hi) {
            m.incComp();
            if (aux[i] <= aux[j]) a[k++] = aux[i++]; else a[k++] = aux[j++];
            m.incWrite(1);
        }
        while (i <= mid) { a[k++] = aux[i++]; m.incWrite(1); }
        while (j <= hi) { a[k++] = aux[j++]; m.incWrite(1); }
    }

    private static void insertion(int[] a, int lo, int hi, Metrics m) {
        for (int i = lo + 1; i <= hi; i++) {
            int v = a[i]; int j = i - 1;
            while (j >= lo) { m.incComp(); if (a[j] <= v) break; a[j + 1] = a[j--]; m.incWrite(1); }
            a[j + 1] = v; m.incWrite(1);
        }
    }
}
