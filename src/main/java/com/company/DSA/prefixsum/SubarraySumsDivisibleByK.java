package com.company.DSA.prefixsum;

/* ============================================================
 *  LeetCode #974 — Subarray Sums Divisible by K
 * ============================================================
 *  PROBLEM
 *  -------
 *  Count subarrays whose sum is divisible by k.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[4,5,0,-2,-3,1], k=5 → 7
 *  Ex2: nums=[5], k=9 → 0
 *  Ex3: nums=[-1,2,9], k=2 → 2
 *
 *  CONSTRAINTS:  1 <= n <= 3*10^4;  -10^4 <= val <= 10^4;  2 <= k <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Count prefix sums BY REMAINDER. Each pair of same remainder → one valid subarray.
 *   2. Normalize negative remainders with ((r % k) + k) % k.
 * ============================================================ */
public class SubarraySumsDivisibleByK {

    public int subarraysDivByK(final int[] numbers, final int divisor) {
        final int[] frequencyByRemainder = new int[divisor];
        frequencyByRemainder[0] = 1;

        int runningPrefixSum = 0;
        int totalCount       = 0;

        for (final int value : numbers) {
            runningPrefixSum += value;
            final int normalizedRemainder = ((runningPrefixSum % divisor) + divisor) % divisor;
            totalCount += frequencyByRemainder[normalizedRemainder]++;
        }
        return totalCount;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Prefix sums modulo k. Pair-counting on each remainder.
 *
 *  Complexity: Time O(n), Space O(k).
 *  Pattern: prefix sum + count by remainder.
 * ============================================================ */
