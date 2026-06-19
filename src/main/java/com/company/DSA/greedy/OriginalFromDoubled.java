package com.company.DSA.greedy;

import java.util.*;

/* ============================================================
 *  LeetCode #2007 — Find Original Array From Doubled Array
 * ============================================================
 *  PROBLEM
 *  -------
 *  Recover original[] from a shuffled doubled-array. Each x in original has
 *  its 2x somewhere too. Return [] if invalid.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: changed=[1,3,4,2,6,8] → [1,3,4]
 *  Ex2: changed=[6,3,0,1]      → []
 *  Ex3: changed=[1]            → []
 *  Ex4: changed=[0,0,0,0]      → [0,0]
 *
 *  CONSTRAINTS:  1 <= n <= 10^5; 0 <= val <= 10^5.
 *
 *  HINTS
 *  -----
 *   1. Sort. Smallest remaining unmatched must be in original.
 * ============================================================ */
public class OriginalFromDoubled {

    public int[] findOriginalArray(final int[] doubledArray) {
        if (doubledArray.length % 2 != 0) {
            return new int[0];
        }

        final Map<Integer, Integer> remainingByValue = new HashMap<>();
        for (final int value : doubledArray) {
            remainingByValue.merge(value, 1, Integer::sum);
        }

        final int[] sortedAscending = doubledArray.clone();
        Arrays.sort(sortedAscending);

        final int[] originalArray = new int[doubledArray.length / 2];
        int writeIndex = 0;

        for (final int value : sortedAscending) {
            if (remainingByValue.getOrDefault(value, 0) == 0) {
                continue;
            }
            if (remainingByValue.getOrDefault(2 * value, 0) == 0) {
                return new int[0];
            }
            originalArray[writeIndex++] = value;
            remainingByValue.merge(value,     -1, Integer::sum);
            remainingByValue.merge(2 * value, -1, Integer::sum);
        }
        return originalArray;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Greedy on sorted input. Smallest unconsumed value must be in original;
 *  pair it with 2x. Fail fast if 2x missing.
 *
 *  Complexity: Time O(n log n), Space O(n).
 *  Pattern: sorted-greedy pair-matching.
 * ============================================================ */
