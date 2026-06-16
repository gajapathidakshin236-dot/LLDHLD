package com.company.DSA;

import java.util.*;

/**
 * LeetCode #39 - Combination Sum (unlimited reuse, distinct candidates)
 * Backtrack with start index; can pick same index again.
 * Time: O(2^t)  Space: O(t)
 */
public class CombinationSum {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> res = new ArrayList<>();
        bt(candidates, 0, target, new ArrayList<>(), res);
        return res;
    }
    private void bt(int[] c, int start, int rem, List<Integer> cur, List<List<Integer>> res) {
        if (rem == 0) { res.add(new ArrayList<>(cur)); return; }
        for (int i = start; i < c.length; i++) {
            if (c[i] > rem) break;
            cur.add(c[i]);
            bt(c, i, rem - c[i], cur, res);
            cur.remove(cur.size() - 1);
        }
    }
}
