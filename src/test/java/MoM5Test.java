import org.junit.jupiter.api.RepeatedTest;
import java.util.Arrays;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class MoM5Test {
    @RepeatedTest(100)
    void select_matches_sorted_kth() {
        Random r = new Random();
        int n = 100 + r.nextInt(300);
        int[] a = r.ints(n, -1000, 1000).toArray();
        int[] gold = a.clone(); Arrays.sort(gold);
        int k = r.nextInt(n);
        Metrics m = new Metrics();
        int v = MoM5.select(a, k, m);
        assertEquals(gold[k], v);

    }
}
