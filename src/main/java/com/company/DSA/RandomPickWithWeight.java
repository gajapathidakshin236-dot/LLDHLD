package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #528 — Random Pick with Weight
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given a positive int array w where w[i] describes the weight of index i,
 *  pick an index at random with probability proportional to w[i].
 *
 *  EXAMPLES
 *  --------
 *  w=[1,3]: index 0 picked 25%, index 1 picked 75%.
 *  w=[1]:   always returns 0.
 *  w=[1,1,1,1]: uniform.
 *
 *  CONSTRAINTS:  1 <= w.length <= 10^4; 1 <= w[i] <= 10^5; <=10^4 pickIndex calls.
 *
 *  HINTS
 *  -----
 *   1. Build prefix sums of w. Total = pref[n-1].
 *   2. Random integer r in [1..total]. Binary search for the first prefix >= r.
 *   3. That index has probability w[i]/total of being chosen.
 * ============================================================ */
public class RandomPickWithWeight {
    private final int[] pref;
    private final Random rnd = new Random();
    public RandomPickWithWeight(int[] w) {
        pref = new int[w.length];
        pref[0] = w[0];
        for (int i = 1; i < w.length; i++) pref[i] = pref[i - 1] + w[i];
    }
    public int pickIndex() {
        int target = rnd.nextInt(pref[pref.length - 1]) + 1;
        int l = 0, r = pref.length - 1;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (pref[m] < target) l = m + 1;
            else r = m;
        }
        return l;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Map weights to consecutive integer ranges. Pick a uniform integer in
 *  [1..total] and find which range it falls into via binary search.
 *  Range for index i is (pref[i-1], pref[i]] of size w[i].
 *
 *  Why pickIndex is O(log n):
 *    Binary search on the prefix array.
 *
 *  Complexity: Build O(n), pickIndex O(log n), Space O(n).
 *  Edge cases: single index → always returns 0.
 *  Pattern: discrete inverse-CDF sampling. Foundation for weighted random sampling.
 * ============================================================ */
