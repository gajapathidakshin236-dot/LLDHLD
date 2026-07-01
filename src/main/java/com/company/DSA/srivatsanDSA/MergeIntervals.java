package com.company.DSA.srivatsanDSA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 50
 *  Problem: Merge Intervals
 *
 *  APPROACH (from notes):
 *    Sort by start.
 *    Walk; if intervals[i].start <= currentMerged.end → extend currentMerged.end.
 *    Else push currentMerged to result and start new currentMerged.
 *    Push the final currentMerged at the end.
 * ============================================================ */
public class MergeIntervals {

    public int[][] merge(final int[][] intervals) {
        if (intervals.length <= 1) {
            return intervals;
        }
        Arrays.sort(intervals, Comparator.comparingInt(interval -> interval[0]));

        final List<int[]> mergedList = new ArrayList<>();
        int[] currentInterval        = intervals[0];

        for (int index = 1; index < intervals.length; index++) {
            final int[] nextInterval = intervals[index];

            if (nextInterval[0] <= currentInterval[1]) {
                currentInterval[1] = Math.max(currentInterval[1], nextInterval[1]);
            } else {
                mergedList.add(currentInterval);
                currentInterval = nextInterval;
            }
        }
        mergedList.add(currentInterval);
        return mergedList.toArray(new int[0][]);
    }
}
