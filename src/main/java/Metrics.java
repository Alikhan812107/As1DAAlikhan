public class Metrics {


    public long comps, swaps, writes, allocs;
    public int maxDepth, curDepth;

    public void reset() { comps = swaps = writes = allocs = 0; maxDepth = curDepth = 0; }
    public void incComp() { comps++; }
    public void incSwap() { swaps++; }
    public void incWrite(int c) { writes += c; }
    public void addAlloc(long bytes) { allocs += bytes; }

    public void enter() { if (++curDepth > maxDepth) maxDepth = curDepth; }
    public void exit() { curDepth--; }

}
