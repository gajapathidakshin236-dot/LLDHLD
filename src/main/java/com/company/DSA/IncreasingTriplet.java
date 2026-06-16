package com.company.DSA;

/* ============================================================
 *  LeetCode #334 — Increasing Triplet Subsequence
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return true if there exists a triple of indices i < j < k such that
 *  nums[i] < nums[j] < nums[k]. O(n) time, O(1) space.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,3,4,5]      → true
 *  Ex2: [5,4,3,2,1]      → false
 *  Ex3: [2,1,5,0,4,6]    → true   (0,4,6)
 *  Ex4: [1,1,1]          → false
 *
 *  CONSTRAINTS:  1 <= n <= 5*10^5;  -2^31 <= val <= 2^31 - 1.
 *
 *  HINTS
 *  -----
 *   1. Track the SMALLEST so far (a) and the SMALLEST-second-after-a (b).
 *   2. On any x > b → triplet exists (we know there's a < b < x).
 *   3. Strict <, not <=.
 * ============================================================ */
public class IncreasingTriplet {
    public boolean increasingTriplet(int[] nums) {
        int a = Integer.MAX_VALUE, b = Integer.MAX_VALUE;
        for (int x : nums) {
            if (x <= a) a = x;
            else if (x <= b) b = x;
            else return true;
        }
        return false;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Maintain two "running mins" with order constraint:
 *    a = smallest seen so far.
 *    b = smallest seen AFTER some smaller value (i.e., guaranteed has an `a < b`).
 *  When we see x > b → there exist a < b < x → return true.
 *
 *  Why x <= a, not x < a:
 *    Updating `a` on equal keeps the relationship correct; equality doesn't
 *    create a strict increasing pair.
 *
 *  Why this finds the answer even if `a` later updates:
 *    `a` may decrease (some later smaller value). That's fine — `b` still has
 *    a witness `prevA` that was less than it; we don't need the SAME a.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Edge cases: length < 3 → false; strictly decreasing → false.
 *  Pattern: O(1)-state tracking of growth chain.
 * ============================================================ */
