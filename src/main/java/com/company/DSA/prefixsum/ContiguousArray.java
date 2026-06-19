package com.company.DSA.prefixsum;

import java.util.*;

/* ============================================================
 *  LeetCode #525 — Contiguous Array
 * ============================================================
 *  PROBLEM
 *  -------
 *  Max length of contiguous subarray with equal 0s and 1s.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [0,1]       → 2
 *  Ex2: [0,1,0]     → 2
 *  Ex3: [0,0,1,0,1,1] → 6
 *
 *  CONSTRAINTS:  1 <= n <= 10^5; nums[i] in {0,1}.
 *
 *  HINTS
 *  -----
 *   1. Map 0 → -1. Equal 0/1 ⇔ subarray sum is 0.
 *   2. Track FIRST occurrence of each running sum.
 * ============================================================ */
public class ContiguousArray {

    public int findMaxLength(final int[] binary) {
        final Map<Integer, Integer> firstIndexBySum = new HashMap<>();
        firstIndexBySum.put(0, -1);

        int runningSum    = 0;
        int bestLength    = 0;

        for (int index = 0; index < binary.length; index++) {
            runningSum += (binary[index] == 0) ? -1 : 1;

            if (firstIndexBySum.containsKey(runningSum)) {
                bestLength = Math.max(bestLength, index - firstIndexBySum.get(runningSum));
            } else {
                firstIndexBySum.put(runningSum, index);
            }
        }
        return bestLength;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Substitute 0 → -1. Same prefix sum appearing again means in-between sums 0.
 *  Track FIRST occurrence to maximize span.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: prefix sum + first-occurrence map.
 * ============================================================ */
