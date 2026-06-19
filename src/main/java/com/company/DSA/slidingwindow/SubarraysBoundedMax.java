package com.company.DSA.slidingwindow;

/* ============================================================
 *  LeetCode #795 — Number of Subarrays with Bounded Maximum
 * ============================================================
 *  PROBLEM
 *  -------
 *  Count contiguous subarrays whose max value lies in [left, right].
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[2,1,4,3], left=2, right=3 → 3
 *  Ex2: nums=[2,9,2,5,6], left=2, right=8 → 7
 *
 *  CONSTRAINTS:  1 <= n <= 10^5;  0 <= val <= 10^9.
 *
 *  HINTS
 *  -----
 *   1. count_max_<= R minus count_max_<= L-1.
 * ============================================================ */
public class SubarraysBoundedMax {

    public int numSubarrayBoundedMax(final int[] numbers,
                                     final int lowerBound,
                                     final int upperBound) {
        return countSubarraysWithMaxAtMost(numbers, upperBound)
             - countSubarraysWithMaxAtMost(numbers, lowerBound - 1);
    }

    private int countSubarraysWithMaxAtMost(final int[] numbers, final int threshold) {
        int totalSubarrays           = 0;
        int currentRunLength         = 0;

        for (final int value : numbers) {
            if (value <= threshold) {
                currentRunLength++;
                totalSubarrays += currentRunLength;
            } else {
                currentRunLength = 0;
            }
        }
        return totalSubarrays;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Convert "max in [L, R]" to (count_max_<=R - count_max_<=L-1). For each
 *  helper count, elements > k act as separators.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: difference-of-counts via "<=k" helper.
 * ============================================================ */
