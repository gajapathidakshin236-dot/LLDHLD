package com.company.DSA.slidingwindow;

import java.util.*;

/* ============================================================
 *  LeetCode #2090 — K Radius Subarray Averages
 * ============================================================
 *  PROBLEM
 *  -------
 *  For each index i, compute average of nums[i-k..i+k] or -1 if window doesn't fit.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[7,4,3,9,1,8,5,2,6], k=3 → [-1,-1,-1,5,4,4,-1,-1,-1]
 *  Ex2: nums=[100000], k=0 → [100000]
 *
 *  CONSTRAINTS:  1 <= n <= 10^5;  0 <= k <= 10^5.
 *
 *  HINTS
 *  -----
 *   1. Window size 2k+1; sliding sum (long).
 * ============================================================ */
public class KRadiusSubarrayAverages {

    public int[] getAverages(final int[] numbers, final int neighborRadius) {
        final int length     = numbers.length;
        final int windowSize = 2 * neighborRadius + 1;
        final int[] averages = new int[length];
        Arrays.fill(averages, -1);

        if (windowSize > length) {
            return averages;
        }

        long currentWindowSum = 0;
        for (int index = 0; index < windowSize; index++) {
            currentWindowSum += numbers[index];
        }
        averages[neighborRadius] = (int) (currentWindowSum / windowSize);

        for (int rightCursor = windowSize; rightCursor < length; rightCursor++) {
            currentWindowSum += numbers[rightCursor] - numbers[rightCursor - windowSize];
            averages[rightCursor - neighborRadius] = (int) (currentWindowSum / windowSize);
        }
        return averages;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Fixed-size sliding window of 2k+1; long accumulator avoids overflow.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: classic moving average.
 * ============================================================ */
