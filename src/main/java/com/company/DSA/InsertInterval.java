package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #57 — Insert Interval
 * ============================================================
 *
 *  PROBLEM
 *  -------
 *  You are given a list of non-overlapping intervals sorted by start, and a
 *  newInterval. Insert newInterval into the list so the result is still
 *  sorted and non-overlapping (merge if necessary). Return the new list.
 *
 *  EXAMPLES
 *  --------
 *  Example 1:
 *    Input:  intervals=[[1,3],[6,9]], newInterval=[2,5]
 *    Output: [[1,5],[6,9]]
 *    Why:    [2,5] overlaps [1,3] → merge to [1,5]; [6,9] stays.
 *
 *  Example 2:
 *    Input:  intervals=[[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval=[4,8]
 *    Output: [[1,2],[3,10],[12,16]]
 *    Why:    [4,8] overlaps [3,5], [6,7], [8,10] all together.
 *
 *  Example 3:
 *    Input:  intervals=[], newInterval=[5,7]
 *    Output: [[5,7]]
 *    Why:    Empty list — just insert.
 *
 *  Example 4:
 *    Input:  intervals=[[1,5]], newInterval=[6,8]
 *    Output: [[1,5],[6,8]]
 *    Why:    No overlap, append at end.
 *
 *  CONSTRAINTS
 *  -----------
 *   0 <= intervals.length <= 10^4
 *   intervals[i].length == 2, sorted by start, non-overlapping
 *   0 <= start_i <= end_i <= 10^5
 *
 *  HINTS
 *  -----
 *   1. The list is already sorted — you don't need to sort again.
 *   2. Think of three phases: BEFORE, OVERLAP, AFTER.
 *   3. While merging overlap, expand newInterval's [lo, hi] to swallow all overlapping ones.
 * ============================================================ */
public class InsertInterval {
    public int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> res = new ArrayList<>();
        int i = 0, n = intervals.length;
        while (i < n && intervals[i][1] < newInterval[0]) res.add(intervals[i++]);
        while (i < n && intervals[i][0] <= newInterval[1]) {
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }
        res.add(newInterval);
        while (i < n) res.add(intervals[i++]);
        return res.toArray(new int[0][]);
    }
}

/* ============================================================
 *  APPROACH EXPLAINED
 * ============================================================
 *
 *  Why this works:
 *    Intervals are sorted by start AND non-overlapping. So any interval
 *    that ends before newInterval starts is in the safe LEFT zone.
 *    Any interval whose start is after newInterval ends is in the safe RIGHT zone.
 *    Everything between is what overlaps with newInterval — we collapse it.
 *
 *  Three phases:
 *    Phase 1 — LEFT (no overlap): intervals[i].end < newInterval.start
 *              → copy them to result.
 *    Phase 2 — OVERLAP: intervals[i].start <= newInterval.end
 *              → expand newInterval.start = min, newInterval.end = max.
 *    Phase 3 — RIGHT (no overlap): the rest → copy as-is.
 *    Finally push the (possibly expanded) newInterval and append the tail.
 *
 *  Why NOT just "append + sort + merge":
 *    That works (#56 approach) but costs O(n log n). This is O(n).
 *
 *  Complexity:
 *    Time:  O(n)  — single pass.
 *    Space: O(n)  — output list.
 *
 *  Edge cases:
 *    - newInterval before everything → phase 1 empty, phase 2 empty, push newInterval, then phase 3.
 *    - newInterval after everything → phase 1 copies all, then push newInterval.
 *    - Empty input → push newInterval only.
 *    - newInterval swallows everything → phase 2 absorbs all.
 *
 *  Pattern:
 *    Sorted scan with three regions. Useful any time input is already sorted
 *    and we're inserting something new that may collide.
 * ============================================================ */
