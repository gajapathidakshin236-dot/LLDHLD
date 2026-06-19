package com.company.DSA.arrays;

/* ============================================================
 *  LeetCode #334 — Increasing Triplet Subsequence
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return true if there's i<j<k with nums[i]<nums[j]<nums[k].
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,3,4,5]      → true
 *  Ex2: [5,4,3,2,1]      → false
 *  Ex3: [2,1,5,0,4,6]    → true
 *  Ex4: [1,1,1]          → false
 *
 *  CONSTRAINTS:  1 <= n <= 5*10^5.
 *
 *  HINTS
 *  -----
 *   1. Track smallest (a) and smallest-second-after-a (b). Any x > b → triplet.
 * ============================================================ */
public class IncreasingTriplet {

    public boolean increasingTriplet(final int[] numbers) {
        int smallestSoFar          = Integer.MAX_VALUE;
        int smallestMiddleSoFar    = Integer.MAX_VALUE;

        for (final int value : numbers) {
            if (value <= smallestSoFar) {
                smallestSoFar = value;
            } else if (value <= smallestMiddleSoFar) {
                smallestMiddleSoFar = value;
            } else {
                return true;
            }
        }
        return false;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Maintain two running mins with order constraint.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: O(1)-state tracking of growth chain.
 * ============================================================ */
