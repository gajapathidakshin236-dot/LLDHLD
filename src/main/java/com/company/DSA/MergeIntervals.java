package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #56 — Merge Intervals
 * ============================================================
 *
 *  PROBLEM
 *  -------
 *  Given an array of intervals where intervals[i] = [start_i, end_i],
 *  merge all overlapping intervals and return an array of the
 *  non-overlapping intervals that cover all the original intervals.
 *  Two intervals overlap if one's start <= the other's end.
 *
 *  EXAMPLES
 *  --------
 *  Example 1:
 *    Input:  [[1,3],[2,6],[8,10],[15,18]]
 *    Output: [[1,6],[8,10],[15,18]]
 *    Why:    [1,3] and [2,6] overlap so we merge to [1,6].
 *
 *  Example 2:
 *    Input:  [[1,4],[4,5]]
 *    Output: [[1,5]]
 *    Why:    Touching intervals (4==4) are treated as overlapping.
 *
 *  Example 3:
 *    Input:  [[1,4],[0,4]]
 *    Output: [[0,4]]
 *    Why:    Order in input isn't sorted — we must sort first.
 *
 *  Example 4:
 *    Input:  [[1,10],[2,3],[4,5],[6,7]]
 *    Output: [[1,10]]
 *    Why:    One large interval absorbs every smaller one inside.
 *
 *  CONSTRAINTS
 *  -----------
 *   1 <= intervals.length <= 10^4
 *   intervals[i].length == 2
 *   0 <= start_i <= end_i <= 10^4
 *
 *  HINTS
 *  -----
 *   1. If intervals are sorted by start, an overlap can only ever happen
 *      with the LAST merged interval — never with anything earlier.
 *   2. Sort by start, then walk once.
 *   3. Use Math.max for the new end (one interval might fully contain another).
 * ============================================================ */
public class MergeIntervals {
    public int[][] merge(int[][] intervals) {
        if (intervals.length <= 1) return intervals;
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        List<int[]> out = new ArrayList<>();
        int[] cur = intervals[0];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= cur[1]) {
                cur[1] = Math.max(cur[1], intervals[i][1]);
            } else {
                out.add(cur);
                cur = intervals[i];
            }
        }
        out.add(cur);
        return out.toArray(new int[0][]);
    }
}

/* ============================================================
 *  APPROACH EXPLAINED
 * ============================================================
 *
 *  Why this works:
 *    After sorting by start, the next interval can only overlap with the
 *    "current" running interval. It cannot reach back further because every
 *    previous interval's start <= current.start.
 *
 *  Step-by-step:
 *    1. Sort intervals by start time.
 *    2. Keep a `cur` interval — the one we are currently extending.
 *    3. For each next interval:
 *         - If next.start <= cur.end → overlap → cur.end = max(cur.end, next.end)
 *         - Else → no overlap → push cur to output and start a new cur.
 *    4. After the loop, push the last cur.
 *
 *  Why naive O(n^2) pairwise merge is bad:
 *    Repeatedly checking every pair is wasteful and complicated to keep correct.
 *    Sort + sweep is one clean pass.
 *
 *  Complexity:
 *    Time:  O(n log n) for sort, O(n) for sweep.
 *    Space: O(n) output, O(log n) sort stack.
 *
 *  Edge cases:
 *    - Single interval → return as-is.
 *    - Touching intervals (1,4) (4,5) → treat <= as overlap so they merge.
 *    - Contained intervals (1,10) (2,3) → max keeps the wider end.
 *
 *  Pattern:
 *    "Sort + sweep" — the canonical interval-problem pattern.
 *    Same idea powers #57 Insert Interval, #252 Meeting Rooms, #435 Non-overlapping.
 * ============================================================ */
