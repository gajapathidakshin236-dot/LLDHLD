package com.company.DSA;

import java.util.*;

/**
 * LeetCode #40 - Combination Sum II (each candidate used once, may have dups)
 * Sort; skip duplicates at the same recursion level.
 * Time: O(2^n)  Space: O(n)
 */
public class CombinationSumII {
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> res = new ArrayList<>();
        bt(candidates, 0, target, new ArrayList<>(), res);
        return res;
    }
    private void bt(int[] c, int start, int rem, List<Integer> cur, List<List<Integer>> res) {
        if (rem == 0) { res.add(new ArrayList<>(cur)); return; }
        for (int i = start; i < c.length; i++) {
            if (i > start && c[i] == c[i - 1]) continue;
            if (c[i] > rem) break;
            cur.add(c[i]);
            bt(c, i + 1, rem - c[i], cur, res);
            cur.remove(cur.size() - 1);
        }
    }
}
