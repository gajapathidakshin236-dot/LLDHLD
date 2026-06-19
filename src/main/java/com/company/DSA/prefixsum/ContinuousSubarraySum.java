package com.company.DSA.prefixsum;

import java.util.*;

/* ============================================================
 *  LeetCode #523 — Continuous Subarray Sum
 * ============================================================
 *  PROBLEM
 *  -------
 *  Is there a contiguous subarray of length >= 2 whose sum is a multiple of k?
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[23,2,4,6,7], k=6 → true
 *  Ex2: nums=[23,2,6,4,7], k=6 → true
 *  Ex3: nums=[23,2,6,4,7], k=13 → false
 *  Ex4: nums=[1,0], k=2 → false
 *  Ex5: nums=[0,0], k=1 → true
 *
 *  CONSTRAINTS:  1 <= n <= 10^5;  0 <= val <= 10^9;  1 <= k <= 2^31 - 1.
 *
 *  HINTS
 *  -----
 *   1. Same remainder twice + gap >= 2 → divisible subarray exists.
 *   2. Store FIRST index per remainder.
 * ============================================================ */
public class ContinuousSubarraySum {

    private static final int MIN_REQUIRED_LENGTH = 2;

    public boolean checkSubarraySum(final int[] numbers, final int divisor) {
        final Map<Integer, Integer> firstIndexByRemainder = new HashMap<>();
        firstIndexByRemainder.put(0, -1);

        int runningPrefixSum = 0;

        for (int index = 0; index < numbers.length; index++) {
            runningPrefixSum += numbers[index];
            final int normalizedRemainder = ((runningPrefixSum % divisor) + divisor) % divisor;

            if (firstIndexByRemainder.containsKey(normalizedRemainder)) {
                if (index - firstIndexByRemainder.get(normalizedRemainder) >= MIN_REQUIRED_LENGTH) {
                    return true;
                }
            } else {
                firstIndexByRemainder.put(normalizedRemainder, index);
            }
        }
        return false;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Prefix sums modulo k; store FIRST index of each remainder for maximum gap.
 *
 *  Complexity: Time O(n), Space O(min(n, k)).
 *  Pattern: prefix remainder hashing.
 * ============================================================ */
