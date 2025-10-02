import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class ClosestPPTTest {
    @Test
    void dc_equals_bruteforce_on_small_n() {
        Random r = new Random(0);
        for (int n : new int[]{10, 50, 200, 1000, 2000}) {
            ClosestPP.Point[] pts = new ClosestPP.Point[n];
            for (int i = 0; i < n; i++) pts[i] = new ClosestPP.Point(r.nextDouble(), r.nextDouble());
            Metrics m1 = new Metrics(), m2 = new Metrics();
            double d1 = ClosestPP.closest(pts, m1);
            double d2 = ClosestPP.brute(pts, m2);
            assertEquals(d2, d1, 1e-9);
        }
    }
}
