package com.company.DSA.slidingwindow;

import java.util.*;

/* ============================================================
 *  LeetCode #1695 — Maximum Erasure Value
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the max sum of a subarray whose elements are all unique.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [4,2,4,5,6] → 17
 *  Ex2: [5,2,1,2,5,2,1,2,5] → 8
 *  Ex3: [1,1,1,1] → 1
 *
 *  CONSTRAINTS:  1 <= n <= 10^5;  1 <= val <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Sliding window with a HashSet of values.
 *   2. On duplicate, shrink left until duplicate removed.
 * ============================================================ */
public class MaxErasureValue {

    public int maximumUniqueSubarray(final int[] numbers) {
        final Set<Integer> currentWindowValues = new HashSet<>();
        int leftCursor       = 0;
        int currentWindowSum = 0;
        int bestWindowSum    = 0;

        for (int rightCursor = 0; rightCursor < numbers.length; rightCursor++) {
            while (currentWindowValues.contains(numbers[rightCursor])) {
                currentWindowValues.remove(numbers[leftCursor]);
                currentWindowSum -= numbers[leftCursor];
                leftCursor++;
            }
            currentWindowValues.add(numbers[rightCursor]);
            currentWindowSum += numbers[rightCursor];
            bestWindowSum = Math.max(bestWindowSum, currentWindowSum);
        }
        return bestWindowSum;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Sliding window over distinct values. Whenever the right side wants to add
 *  a duplicate, shrink left until duplicate removed.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: variable window + uniqueness invariant.
 * ============================================================ */
