import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class SortTest {

    @RepeatedTest(10)
    void merge_and_quick_match_JDK_sort_on_random() {
        int n = 10_000;
        Random r = new Random();
        int[] a = r.ints(n, -10000, 10000).toArray();
        int[] b = a.clone(), c = a.clone(), gold = a.clone();
        Metrics m1 = new Metrics(), m2 = new Metrics();
        MergeSort.sort(b, m1, 16);
        QuickSort.sort(c, m2, 16);
        Arrays.sort(gold);
        assertArrayEquals(gold, b);
        assertArrayEquals(gold, c);
    }

    @Test
    void quicksort_depth_is_log_bounded_typically() {
        int n = 200_000;
        int[] a = new Random(0).ints(n).toArray();
        Metrics m = new Metrics();
        QuickSort.sort(a, m, 16);
        int bound = (int) (2 * Math.floor(Math.log(n) / Math.log(2))) + 20;
        assertTrue(m.maxDepth <= bound, "depth=" + m.maxDepth + " bound=" + bound);
    }
}
