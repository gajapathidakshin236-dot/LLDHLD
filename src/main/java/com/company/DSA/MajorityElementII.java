package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #229 — Majority Element II
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return all elements that appear more than n/3 times. O(1) space.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [3,2,3] → [3]
 *  Ex2: [1]     → [1]
 *  Ex3: [1,2]   → [1,2]
 *  Ex4: [1,1,1,3,3,2,2,2] → [1,2]
 *
 *  CONSTRAINTS:  1 <= n <= 5*10^4;  -10^9 <= val <= 10^9.
 *
 *  HINTS
 *  -----
 *   1. There can be at most 2 elements with frequency > n/3.
 *   2. Boyer-Moore generalized: track 2 candidates and their counts.
 *   3. Verify both candidates with a second pass.
 * ============================================================ */
public class MajorityElementII {
    public List<Integer> majorityElement(int[] nums) {
        int c1 = 0, c2 = 1, n1 = 0, n2 = 0;
        for (int x : nums) {
            if (x == c1) n1++;
            else if (x == c2) n2++;
            else if (n1 == 0) { c1 = x; n1 = 1; }
            else if (n2 == 0) { c2 = x; n2 = 1; }
            else { n1--; n2--; }
        }
        n1 = 0; n2 = 0;
        for (int x : nums) { if (x == c1) n1++; else if (x == c2) n2++; }
        List<Integer> res = new ArrayList<>();
        if (n1 > nums.length / 3) res.add(c1);
        if (n2 > nums.length / 3) res.add(c2);
        return res;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Boyer-Moore Majority Vote (generalized for n/3 case):
 *    For majority > n/2, 1 candidate; for > n/3, at most 2 candidates.
 *    Track c1, c2 and their counts. On a third distinct value, decrement both
 *    counts (pairing it off with one slot from each).
 *  Second pass verifies — candidates aren't guaranteed to be over threshold.
 *
 *  Why c1, c2 distinct init:
 *    Avoid both candidates being the same uninitialized value (0). Using
 *    c1=0, c2=1 ensures they start distinct.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Edge cases: small arrays; no majority; both candidates valid.
 *  Pattern: vote-and-cancel. Generalizes to k candidates for > n/(k+1).
 * ============================================================ */
