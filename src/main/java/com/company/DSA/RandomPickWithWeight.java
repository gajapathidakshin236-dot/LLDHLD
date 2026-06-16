package com.company.DSA;

import java.util.*;

/**
 * LeetCode #528 - Random Pick with Weight
 * Build prefix sums; pick r in [1..total]; binary search first prefix >= r.
 * Time: ctor O(n), pick O(log n)  Space: O(n)
 */
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
