package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #39 — Combination Sum
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given distinct candidates and a target, return all unique combinations
 *  where chosen numbers sum to target. Each candidate may be chosen UNLIMITED times.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: candidates=[2,3,6,7], target=7 → [[2,2,3],[7]]
 *  Ex2: candidates=[2,3,5],  target=8 → [[2,2,2,2],[2,3,3],[3,5]]
 *  Ex3: candidates=[2],      target=1 → []
 *  Ex4: candidates=[1],      target=2 → [[1,1]]
 *
 *  CONSTRAINTS:  1 <= n <= 30; values distinct, 2..40; target 1..40.
 *
 *  HINTS
 *  -----
 *   1. Backtrack with a START index (allow same index again to reuse a number).
 *   2. Sort + break on c[i] > rem to prune.
 * ============================================================ */
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

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Backtracking. The start index is reused at the same value to allow repeats,
 *  but never goes BACKWARDS — this enforces a canonical non-decreasing order
 *  in the combination, eliminating duplicates like [2,3] and [3,2].
 *
 *  Sort + break:
 *    Once c[i] > rem, all later c[j] are also > rem (sorted) → break loop.
 *
 *  Complexity: Time O(2^t) worst; depends on target / smallest candidate.
 *  Space: O(target/min(c)) recursion depth.
 *  Edge cases: target < min candidate → []; single candidate with target multiple of it.
 *  Pattern: backtracking with reuse + canonical order. Building block of subset-sum / combination problems.
 * ============================================================ */
