import java.util.*;

public class ClosestPP {
    public static class Point {
        public final double x, y;
        public Point(double x, double y) { this.x = x; this.y = y; }
    }

    public static double closest(Point[] pts, Metrics m) {
        Point[] px = pts.clone();
        Arrays.sort(px, (p, q) -> { m.incComp(); return Double.compare(p.x, q.x); });
        Point[] py = px.clone();
        Arrays.sort(py, (p, q) -> { m.incComp(); return Double.compare(p.y, q.y); });
        return rec(px, py, 0, px.length - 1, m);
    }

    private static double rec(Point[] px, Point[] py, int lo, int hi, Metrics m) {
        m.enter();
        try {
            int n = hi - lo + 1;
            if (n <= 3) {
                double best = Double.POSITIVE_INFINITY;
                for (int i = lo; i <= hi; i++)
                    for (int j = i + 1; j <= hi; j++) {
                        m.incComp();
                        double d = dist(px[i], px[j]);
                        if (d < best) best = d;
                    }
                return best;
            }

            int mid = (lo + hi) >>> 1;
            double midx = px[mid].x;

            List<Point> pyl = new ArrayList<>(n), pyr = new ArrayList<>(n);
            for (Point p : py) { if (p.x <= midx) pyl.add(p); else pyr.add(p); }

            double dl = rec(px, pyl.toArray(new Point[0]), lo, mid, m);
            double dr = rec(px, pyr.toArray(new Point[0]), mid + 1, hi, m);
            double d = Math.min(dl, dr);

            List<Point> strip = new ArrayList<>();
            for (Point p : py) if (Math.abs(p.x - midx) < d) strip.add(p);

            for (int i = 0; i < strip.size(); i++) {
                for (int j = i + 1; j < Math.min(i + 8, strip.size()); j++) {
                    m.incComp();
                    double s = dist(strip.get(i), strip.get(j));
                    if (s < d) d = s;
                }
            }
            return d;
        } finally { m.exit(); }
    }

    private static double dist(Point a, Point b) {
        double dx = a.x - b.x, dy = a.y - b.y;
        return Math.hypot(dx, dy);
    }

    // O(n^2) version for testing
    public static double brute(Point[] pts, Metrics m) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = 0; i < pts.length; i++)
            for (int j = i + 1; j < pts.length; j++) {
                m.incComp();
                double d = dist(pts[i], pts[j]);
                if (d < best) best = d;
            }
        return best;
    }
}
