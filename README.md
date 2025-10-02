 Assignment 1 — Divide & Conquer: Short Technical Report

 1) Objective

Implement four divide-and-conquer algorithms, instrument them with metrics, and evaluate correctness, complexity, and practical behavior:

* MergeSort (MS)
* QuickSort (QS, randomized pivot, recurse on smaller side, cutoff→insertion)
* Deterministic Selection (Median-of-Medians, MoM5)
* Closest Pair of Points in 2D (CPP)

 2) Instrumentation (Metrics)

A single Metrics object is passed into every algorithm to record:

* comps – key comparisons (e.g., <, <=, Double.compare)
* swaps – pairwise exchanges (QS/MoM partition)
* writes – assignments into arrays/buffers (captures data movement)
* allocs – bytes allocated for large helpers (e.g., MergeSort buffer)
* maxDepth – maximum recursion depth (enter()/exit() around recursive frames)

These counts explain *why* an implementation is fast/slow beyond asymptotics.

 3) Algorithms (Design + Recurrences)

 3.1 MergeSort (stable, reusable buffer, cutoff)

 Idea: Sort left and right halves, then merge two sorted lists in linear time.
 Optimizations:

  1. Single reusable aux buffer → one Θ(n) allocation.
  2. Small subarrays (≤ cutoff) use insertion sort.
  3. Skip merge if a[mid] ≤ a[mid+1] (already ordered).
     Recurrence:
  ( T(n) = 2T(n/2) + \Theta(n) \Rightarrow T(n) = \Theta(n\log n) )
  Depth (D(n) = \lfloor \log_2 n \rfloor).
  Extra space (S(n) = \Theta(n)) (one buffer).
Metric signature: high writes during merges, linear comps per merge level, one big alloc.

 3.2 QuickSort (random pivot, smaller-first recursion)

Idea: Partition around a pivot; recurse only on the smaller side and iterate on the larger to bound stack.
Cutoff: insertion sort for tiny partitions.
Expected recurrence (random):
  ( [T(n)] = 2[T(n/2)] + Theta(n)  Theta(n\log n) )
  Depth (typical) (D(n) (\log n)) with smaller-first recursion.
Worst case: (T(n)=Theta(n^2)), but randomized pivot makes it rare.
Metric signature: swaps + writes from partition; comps ≈ linear per level; maxDepth ≪ n.

 3.3 Deterministic Select (Median-of-Medians, groups of 5)
Idea: Group by 5, take medians, recursively select the median of medians as a good pivot; partition; recurse only into the side containing the k-th.
Recurrence (classic bound):
  ( T(n)  T( n/5 ) + T(7n/10) + (n)  T(n)=(n) )
  Metric signature: linear comps/writes; swaps from partition; depth sub-linear and never explosive.

 3.4 Closest Pair of Points (2D) — Divide, conquer, strip

Idea: Sort by x; split; solve recursively; examine a strip near the split, sorted by y; check ≤ 7–8 neighbors per point.
Recurrence:
  ( T(n) = 2T(n/2) + (n) (n\log n) )
Metric signature: comparisons from sorting (pre-step) and constant-factor checks in strip; depth ≈ (\log n); negligible extra allocations if arrays are reused.

 4) Correctness Strategy (JUnit 5)

Sorting: Compare output of MS/QS to Arrays.sort on random and adversarial arrays (empty, size 1, equal keys, sorted, reverse, many duplicates).
Depth bound (QS): Assert maxDepth ≤ c·⌊log₂ n⌋ + b on random arrays (e.g., 2·log₂ n + 20).
Select: For random arrays, verify MoM5.select(a,k) == Arrays.sort(a)[k].
Closest Pair: For n ≤ 2000, assert D&C distance equals brute force within 1e-9.

 5) Experimental Method (CSV for plots)

For each algorithm and size ( n ):

1. Generate random input with fixed seed.
2. Warm up JVM (multiple trials).
3. Measure wall-clock time outside the algorithm; inside, collect metrics.
4. Output CSV rows:

   
   algo,n,time_ms,depth,comps,swaps,writes,allocs
   
5. Plot time vs. n; depth vs. n; for sorts also plot comps/writes vs. n.

 6) Expected Results & Interpretation (concise)

* **MS vs QS (random input):**
  Both (\Theta(n\log n)) time; MS shows **higher writes** (copy + merge) and one big **alloc**; QS shows **swaps** and typically smaller **writes**; **depth**: MS ≈ (\log n), QS ≈ ( \approx ) (1.2–2.0)·(\log n) with smaller-first recursion.
* **Cutoff effect:** Increasing cutoff (e.g., 8→32) usually **reduces time** by lowering recursion overhead; **writes** and **comps** in insertion blocks rise slightly but net time improves.
* **MoM5:** Linear trend in time and metrics; more constant overhead than randomized select, but **worst-case linear** and robust.
* **CPP:** Near-linearithmic time; strip checks stay bounded (~7–8 neighbors), depth ≈ (\log n); sorting dominates early.

 7) Complexity Summary (table)

| Algorithm                       |             Time (avg) |           Time (worst) |                     Space (extra) | Stable | Notes                               |
| ------------------------------- | ---------------------: | ---------------------: | --------------------------------: | ------ | ----------------------------------- |
| MergeSort                       |      (Theta(n\log n)) |      (Theta(n\log n)) |                       (Theta(n)) | Yes    | Single reusable buffer, high writes |
| QuickSort (rand, smaller-first) |      (Theta(n\log n)) |          (Theta(n^2)) |  (\Theta(\log n)) stack (typical) | No     | Depth bounded in practice           |
| Select (MoM5)                   |       ((n)) |       ((n)) | ((1)) extra (in-place) | N/A    | Deterministic pivot quality         |
| Closest Pair (2D)               | (\(n\log n)) | ((n\log n)) | ((n)) for sorted views | N/A    | Strip check constant bound          |

 8) Threats to Validity

* JVM warm-up/JIT: time improves after first runs.
* GC noise: large arrays may trigger GC; prefer reusable buffers.
* Input distribution: worst-case for QS is rare with random pivot but can be forced; report both.
* Measurement: use multiple trials and median/mean.

 9) Conclusions

Metrics make the mechanics visible:

* MS trades writes + buffer for stability and predictable (n\log n).
* QS with smaller-first recursion achieves log-like depth and excellent constants on random data.
* MoM5 gives deterministic linear selection, valuable when worst-case guarantees matter.
* CPP achieves (\mathcal{O}(n\log n)) by limiting strip work to a constant neighbor count.

This aligns theory (recurrences) with observations (CSV metrics), justifying design choices like buffer reuse, smaller-first recursion, and insertion cutoffs.
