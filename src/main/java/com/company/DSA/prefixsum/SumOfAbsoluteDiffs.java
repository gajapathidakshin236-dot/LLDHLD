package com.company.DSA.prefixsum;

/* ============================================================
 *  LeetCode #1685 — Sum of Absolute Differences in a Sorted Array
 * ============================================================
 *  PROBLEM
 *  -------
 *  For each i, return sum of |nums[i] - nums[j]| for all j != i.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [2,3,5] → [4,3,5]
 *  Ex2: [1,4,6,8,10] → [24,15,13,15,21]
 *
 *  CONSTRAINTS:  2 <= n <= 10^5; sorted ascending.
 *
 *  HINTS
 *  -----
 *   1. Split into LEFT (smaller) and RIGHT (larger).
 *   2. left_contribution = i*nums[i] - prefixLeft.
 *   3. right_contribution = suffixRight - (n-1-i)*nums[i].
 * ============================================================ */
public class SumOfAbsoluteDiffs {

    public int[] getSumAbsoluteDifferences(final int[] sortedNumbers) {
        final int length     = sortedNumbers.length;
        final int[] result   = new int[length];

        long totalSum = 0;
        for (final int value : sortedNumbers) {
            totalSum += value;
        }

        long prefixSumLeft = 0;
        for (int index = 0; index < length; index++) {
            final long value           = sortedNumbers[index];
            final long sumOfLargerValues = totalSum - prefixSumLeft - value;

            final long leftContribution  = (long) index * value - prefixSumLeft;
            final long rightContribution = sumOfLargerValues - (long) (length - 1 - index) * value;

            result[index] = (int) (leftContribution + rightContribution);
            prefixSumLeft += value;
        }
        return result;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Sorted array lets us close-form the left/right contributions per index.
 *  Running prefixSum updates as we walk.
 *
 *  Complexity: Time O(n), Space O(n) output only.
 *  Pattern: prefix-sum on sorted data → O(1) per-index totals.
 * ============================================================ */
