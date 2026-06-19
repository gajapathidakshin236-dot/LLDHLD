package com.company.DSA.hashing;

import java.util.*;

/* ============================================================
 *  LeetCode #128 — Longest Consecutive Sequence
 * ============================================================
 *  PROBLEM
 *  -------
 *  Length of longest run of consecutive integers in unsorted nums. O(n).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [100,4,200,1,3,2] → 4
 *  Ex2: [0,3,7,2,5,8,4,6,0,1] → 9
 *  Ex3: [] → 0
 *  Ex4: [1,2,0,1] → 3
 *
 *  CONSTRAINTS:  0 <= n <= 10^5.
 *
 *  HINTS
 *  -----
 *   1. HashSet; only kick off counting from numbers with no predecessor.
 * ============================================================ */
public class LongestConsecutiveSequence {

    public int longestConsecutive(final int[] numbers) {
        final Set<Integer> distinctValues = new HashSet<>();
        for (final int value : numbers) {
            distinctValues.add(value);
        }

        int longestRunLength = 0;

        for (final int value : distinctValues) {
            final boolean isRunStart = !distinctValues.contains(value - 1);
            if (!isRunStart) {
                continue;
            }
            int currentRunLength = 1;
            int currentValue     = value;

            while (distinctValues.contains(currentValue + 1)) {
                currentValue++;
                currentRunLength++;
            }
            longestRunLength = Math.max(longestRunLength, currentRunLength);
        }
        return longestRunLength;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  HashSet for O(1) membership. Only walk forward from run starts to keep
 *  total ops O(n).
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: HashSet pivot + walk only from anchors.
 * ============================================================ */
