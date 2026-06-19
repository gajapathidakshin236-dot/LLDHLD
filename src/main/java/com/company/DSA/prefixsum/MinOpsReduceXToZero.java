package com.company.DSA.prefixsum;

/* ============================================================
 *  LeetCode #1658 — Min Ops to Reduce X to Zero
 * ============================================================
 *  PROBLEM
 *  -------
 *  Remove one end element at a time, subtracting from x. Min removals to hit 0.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[1,1,4,2,3], x=5 → 2
 *  Ex2: nums=[5,6,7,8,9], x=4 → -1
 *  Ex3: nums=[3,2,20,1,1,3], x=10 → 5
 *
 *  CONSTRAINTS:  1 <= n <= 10^5; 1 <= val <= 10^4; 1 <= x <= 10^9.
 *
 *  HINTS
 *  -----
 *   1. Reframe: find LONGEST middle subarray with sum = totalSum - x.
 *   2. Sliding window since values are positive.
 * ============================================================ */
public class MinOpsReduceXToZero {

    public int minOperations(final int[] numbers, final int targetReduction) {
        final int totalSum  = sumOf(numbers);
        final int requiredMiddleSum = totalSum - targetReduction;

        if (requiredMiddleSum < 0) {
            return -1;
        }

        int leftCursor       = 0;
        int currentWindowSum = 0;
        int longestMiddleLength = -1;

        for (int rightCursor = 0; rightCursor < numbers.length; rightCursor++) {
            currentWindowSum += numbers[rightCursor];
            while (currentWindowSum > requiredMiddleSum) {
                currentWindowSum -= numbers[leftCursor++];
            }
            if (currentWindowSum == requiredMiddleSum) {
                longestMiddleLength = Math.max(longestMiddleLength, rightCursor - leftCursor + 1);
            }
        }
        return (longestMiddleLength < 0) ? -1 : numbers.length - longestMiddleLength;
    }

    private int sumOf(final int[] numbers) {
        int total = 0;
        for (final int value : numbers) {
            total += value;
        }
        return total;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Equivalent: find longest middle subarray with sum = total - x. Sliding window.
 *  Answer: n - longestMiddleLength.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: complement reformulation.
 * ============================================================ */
