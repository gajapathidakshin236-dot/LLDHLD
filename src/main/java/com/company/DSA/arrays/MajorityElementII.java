package com.company.DSA.arrays;

import java.util.*;

/* ============================================================
 *  LeetCode #229 — Majority Element II
 * ============================================================
 *  PROBLEM
 *  -------
 *  Elements appearing more than n/3 times. O(1) space.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [3,2,3] → [3]
 *  Ex2: [1]     → [1]
 *  Ex3: [1,2]   → [1,2]
 *  Ex4: [1,1,1,3,3,2,2,2] → [1,2]
 *
 *  CONSTRAINTS:  1 <= n <= 5*10^4.
 *
 *  HINTS
 *  -----
 *   1. At most 2 candidates. Boyer-Moore + verification pass.
 * ============================================================ */
public class MajorityElementII {

    public List<Integer> majorityElement(final int[] numbers) {
        int firstCandidate  = 0;
        int secondCandidate = 1;
        int firstCount      = 0;
        int secondCount     = 0;

        for (final int value : numbers) {
            if (value == firstCandidate) {
                firstCount++;
            } else if (value == secondCandidate) {
                secondCount++;
            } else if (firstCount == 0) {
                firstCandidate = value;
                firstCount     = 1;
            } else if (secondCount == 0) {
                secondCandidate = value;
                secondCount     = 1;
            } else {
                firstCount--;
                secondCount--;
            }
        }

        firstCount  = 0;
        secondCount = 0;
        for (final int value : numbers) {
            if (value == firstCandidate)       { firstCount++;  }
            else if (value == secondCandidate) { secondCount++; }
        }

        final List<Integer> results = new ArrayList<>();
        final int threshold = numbers.length / 3;
        if (firstCount  > threshold) { results.add(firstCandidate);  }
        if (secondCount > threshold) { results.add(secondCandidate); }
        return results;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Generalized Boyer-Moore for n/3. Track 2 candidates + counts; on a third
 *  distinct value decrement both. Verify with a second pass.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: vote-and-cancel.
 * ============================================================ */
