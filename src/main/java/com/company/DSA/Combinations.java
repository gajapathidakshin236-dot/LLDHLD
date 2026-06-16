package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #77 — Combinations
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return all possible combinations of k numbers chosen from 1..n.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: n=4, k=2 → [[1,2],[1,3],[1,4],[2,3],[2,4],[3,4]]
 *  Ex2: n=1, k=1 → [[1]]
 *  Ex3: n=5, k=3 → 10 combinations.
 *
 *  CONSTRAINTS:  1 <= k <= n <= 20.
 *
 *  HINTS
 *  -----
 *   1. Backtrack starting from current `start` up to n.
 *   2. Prune: stop loop when remaining elements can't fill k - cur.size().
 *      That is, `i <= n - (k - cur.size()) + 1`.
 * ============================================================ */
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

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Backtrack with monotonic start index → enforces ascending order, dedupes.
 *  Prune the loop upper bound to avoid descents that can't finish (not enough
 *  remaining numbers to fill k).
 *
 *  Complexity: Time O(C(n,k) * k), Space O(k).
 *  Pattern: backtracking with combinatorial pruning.
 * ============================================================ */
