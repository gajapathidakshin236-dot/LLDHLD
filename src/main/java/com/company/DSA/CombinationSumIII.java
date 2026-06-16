package com.company.DSA;

import java.util.*;

/**
 * LeetCode #216 - Combination Sum III
 * Choose k distinct nums from 1..9 summing to n. Backtrack.
 * Time: O(C(9,k))  Space: O(k)
 */
public class CombinationSumIII {
    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> res = new ArrayList<>();
        bt(1, k, n, new ArrayList<>(), res);
        return res;
    }
    private void bt(int start, int k, int rem, List<Integer> cur, List<List<Integer>> res) {
        if (cur.size() == k) { if (rem == 0) res.add(new ArrayList<>(cur)); return; }
        for (int i = start; i <= 9; i++) {
            if (i > rem) break;
            cur.add(i);
            bt(i + 1, k, rem - i, cur, res);
            cur.remove(cur.size() - 1);
        }
    }
}
