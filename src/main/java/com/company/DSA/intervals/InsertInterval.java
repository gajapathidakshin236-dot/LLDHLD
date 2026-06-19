package com.company.DSA.intervals;

import java.util.*;

/* ============================================================
 *  LeetCode #57 — Insert Interval
 * ============================================================
 *  PROBLEM
 *  -------
 *  Insert a new interval into a sorted non-overlapping list and merge.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: intervals=[[1,3],[6,9]], newInterval=[2,5] → [[1,5],[6,9]]
 *  Ex2: [[1,2],[3,5],[6,7],[8,10],[12,16]], [4,8] → [[1,2],[3,10],[12,16]]
 *  Ex3: [], [5,7] → [[5,7]]
 *  Ex4: [[1,5]], [6,8] → [[1,5],[6,8]]
 *
 *  CONSTRAINTS:  0 <= n <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Three phases: copy LEFT, merge OVERLAP, copy RIGHT.
 * ============================================================ */
public class InsertInterval {

    private static final int START_FIELD = 0;
    private static final int END_FIELD   = 1;

    public int[][] insert(final int[][] sortedIntervals, final int[] newInterval) {
        final List<int[]> resultList = new ArrayList<>();
        int cursor                   = 0;

        cursor = copyIntervalsBefore(sortedIntervals, newInterval, cursor, resultList);
        cursor = mergeOverlappingIntervalsInto(sortedIntervals, newInterval, cursor);
        resultList.add(newInterval);
        copyRemaining(sortedIntervals, cursor, resultList);

        return resultList.toArray(new int[0][]);
    }

    private int copyIntervalsBefore(final int[][] sortedIntervals,
                                    final int[] newInterval,
                                    int cursor,
                                    final List<int[]> resultList) {
        while (cursor < sortedIntervals.length
                && sortedIntervals[cursor][END_FIELD] < newInterval[START_FIELD]) {
            resultList.add(sortedIntervals[cursor++]);
        }
        return cursor;
    }

    private int mergeOverlappingIntervalsInto(final int[][] sortedIntervals,
                                              final int[] newInterval,
                                              int cursor) {
        while (cursor < sortedIntervals.length
                && sortedIntervals[cursor][START_FIELD] <= newInterval[END_FIELD]) {
            newInterval[START_FIELD] = Math.min(newInterval[START_FIELD], sortedIntervals[cursor][START_FIELD]);
            newInterval[END_FIELD]   = Math.max(newInterval[END_FIELD],   sortedIntervals[cursor][END_FIELD]);
            cursor++;
        }
        return cursor;
    }

    private void copyRemaining(final int[][] sortedIntervals,
                               int cursor,
                               final List<int[]> resultList) {
        while (cursor < sortedIntervals.length) {
            resultList.add(sortedIntervals[cursor++]);
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Three phases: copy LEFT (no overlap), merge OVERLAPPING by expanding newInterval,
 *  copy RIGHT (no overlap).
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: sorted scan with three regions.
 * ============================================================ */
