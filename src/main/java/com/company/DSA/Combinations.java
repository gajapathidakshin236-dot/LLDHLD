package com.company.DSA;

import java.util.*;

/**
 * LeetCode #77 - Combinations C(n, k)
 * Backtrack with start; stop early if remaining can't fill k.
 * Time: O(C(n,k) * k)  Space: O(k)
 */
public class Combinations {
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();
        bt(1, n, k, new ArrayList<>(), res);
        return res;
    }
    private void bt(int start, int n, int k, List<Integer> cur, List<List<Integer>> res) {
        if (cur.size() == k) { res.add(new ArrayList<>(cur)); return; }
        int need = k - cur.size();
        for (int i = start; i <= n - need + 1; i++) {
            cur.add(i);
            bt(i + 1, n, k, cur, res);
            cur.remove(cur.size() - 1);
        }
    }
}
