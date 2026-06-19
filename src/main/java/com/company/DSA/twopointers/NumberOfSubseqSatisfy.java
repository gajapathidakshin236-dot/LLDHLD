package com.company.DSA.twopointers;

import java.util.*;

/* ============================================================
 *  LeetCode #1498 — Number of Subsequences That Satisfy the Given Sum Condition
 * ============================================================
 *  PROBLEM
 *  -------
 *  Count non-empty subsequences with min+max <= target. Modulo 1e9+7.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[3,5,6,7], target=9 → 4
 *  Ex2: nums=[3,3,6,8], target=10 → 6
 *  Ex3: nums=[2,3,3,4,6,7], target=12 → 61
 *
 *  CONSTRAINTS:  1 <= n <= 10^5; 1 <= val/target <= 10^6.
 *
 *  HINTS
 *  -----
 *   1. Only MIN and MAX matter → sort.
 *   2. Two pointers; count += 2^(r-l) when a[l]+a[r] <= target.
 * ============================================================ */
public class NumberOfSubseqSatisfy {

    private static final int MODULUS = 1_000_000_007;

    public int numSubseq(final int[] numbers, final int target) {
        final int length = numbers.length;
        Arrays.sort(numbers);

        final int[] powerOfTwo = precomputePowersOfTwo(length);

        int leftCursor  = 0;
        int rightCursor = length - 1;
        int totalCount  = 0;

        while (leftCursor <= rightCursor) {
            if (numbers[leftCursor] + numbers[rightCursor] <= target) {
                totalCount = (totalCount + powerOfTwo[rightCursor - leftCursor]) % MODULUS;
                leftCursor++;
            } else {
                rightCursor--;
            }
        }
        return totalCount;
    }

    private int[] precomputePowersOfTwo(final int length) {
        final int[] powerOfTwo = new int[length];
        powerOfTwo[0] = 1;
        for (int exponent = 1; exponent < length; exponent++) {
            powerOfTwo[exponent] = (int) ((long) powerOfTwo[exponent - 1] * 2 % MODULUS);
        }
        return powerOfTwo;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Sort first since only min/max matter. Two pointers: when a[l]+a[r] <= target,
 *  every 2^(r-l) subset of the in-between is valid.
 *
 *  Complexity: Time O(n log n), Space O(n).
 *  Pattern: sort + two pointers + precomputed powers.
 * ============================================================ */
