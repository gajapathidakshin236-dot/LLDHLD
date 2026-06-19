package com.company.DSA.prefixsum;

/* ============================================================
 *  LeetCode #1567 — Maximum Length of Subarray With Positive Product
 * ============================================================
 *  PROBLEM
 *  -------
 *  Max length of contiguous subarray whose product is positive.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,-2,-3,4] → 4
 *  Ex2: [0,1,-2,-3,-4] → 3
 *  Ex3: [-1,-2,-3,0,1] → 2
 *  Ex4: [-1] → 0
 *
 *  CONSTRAINTS:  1 <= n <= 10^5;  -10^9 <= val <= 10^9.
 *
 *  HINTS
 *  -----
 *   1. Track posLen and negLen (lengths of subs ending here with pos / neg product).
 *   2. On 0 reset both; on neg swap roles.
 * ============================================================ */
public class MaxLenPositiveProduct {

    public int getMaxLen(final int[] numbers) {
        int lengthEndingPositive = 0;
        int lengthEndingNegative = 0;
        int bestPositiveLength   = 0;

        for (final int value : numbers) {
            if (value == 0) {
                lengthEndingPositive = 0;
                lengthEndingNegative = 0;
            } else if (value > 0) {
                lengthEndingPositive++;
                lengthEndingNegative = (lengthEndingNegative > 0) ? lengthEndingNegative + 1 : 0;
            } else {
                final int previousPositive = lengthEndingPositive;
                lengthEndingPositive = (lengthEndingNegative > 0) ? lengthEndingNegative + 1 : 0;
                lengthEndingNegative = previousPositive + 1;
            }
            bestPositiveLength = Math.max(bestPositiveLength, lengthEndingPositive);
        }
        return bestPositiveLength;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Parity-state DP: track lengths of pos/neg-product subarrays ending here.
 *  Multiply by current sign:
 *    pos value: pos extends, neg extends if non-zero.
 *    neg value: roles swap (using previous pos/neg).
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: parity-state DP.
 * ============================================================ */
