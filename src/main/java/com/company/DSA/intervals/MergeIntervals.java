package com.company.DSA.intervals;

import java.util.*;

/* ============================================================
 *  LeetCode #56 — Merge Intervals
 * ============================================================
 *  PROBLEM
 *  -------
 *  Merge all overlapping intervals; return non-overlapping coverage.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [[1,3],[2,6],[8,10],[15,18]] → [[1,6],[8,10],[15,18]]
 *  Ex2: [[1,4],[4,5]] → [[1,5]]
 *  Ex3: [[1,4],[0,4]] → [[0,4]]
 *  Ex4: [[1,10],[2,3],[4,5],[6,7]] → [[1,10]]
 *
 *  CONSTRAINTS:  1 <= n <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Sort by start; sweep; extend `current` while overlapping.
 * ============================================================ */
public class MergeIntervals {

    private static final int START_FIELD = 0;
    private static final int END_FIELD   = 1;

    public int[][] merge(final int[][] intervals) {
        if (intervals.length <= 1) {
            return intervals;
        }

        Arrays.sort(intervals, Comparator.comparingInt(interval -> interval[START_FIELD]));

        final List<int[]> mergedList = new ArrayList<>();
        int[] currentInterval        = intervals[0];

        for (int index = 1; index < intervals.length; index++) {
            final int[] nextInterval = intervals[index];

            if (nextInterval[START_FIELD] <= currentInterval[END_FIELD]) {
                currentInterval[END_FIELD] = Math.max(currentInterval[END_FIELD], nextInterval[END_FIELD]);
            } else {
                mergedList.add(currentInterval);
                currentInterval = nextInterval;
            }
        }
        mergedList.add(currentInterval);

        return mergedList.toArray(new int[0][]);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Sort by start, then sweep extending the running interval whenever next.start
 *  <= current.end. Otherwise close current and start anew.
 *
 *  Complexity: Time O(n log n), Space O(n).
 *  Pattern: sort + sweep.
 * ============================================================ */
