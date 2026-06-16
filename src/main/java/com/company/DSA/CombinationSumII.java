package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #40 — Combination Sum II
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given candidates with possible DUPLICATES and a target, return all unique
 *  combinations summing to target. Each candidate may be used AT MOST ONCE.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [10,1,2,7,6,1,5], target=8 → [[1,1,6],[1,2,5],[1,7],[2,6]]
 *  Ex2: [2,5,2,1,2],      target=5 → [[1,2,2],[5]]
 *  Ex3: [1,1,1],          target=2 → [[1,1]]
 *
 *  CONSTRAINTS:  1 <= n <= 100;  1 <= val <= 50;  1 <= target <= 30.
 *
 *  HINTS
 *  -----
 *   1. Sort. Dups become adjacent.
 *   2. Backtrack with i+1 (use-each-once) but skip identical NEIGHBORS at the
 *      same depth: `if (i > start && c[i] == c[i-1]) continue;`
 *   3. Break loop when c[i] > rem.
 * ============================================================ */
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

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Like #39 but each candidate is used once (advance to i+1, not i).
 *  Dedupe rule: skip nums[i] if equal to nums[i-1] at the SAME RECURSION DEPTH
 *  (`i > start`). Across depths, equal values may legitimately reappear.
 *
 *  Complexity: Time O(2^n) worst, much better with pruning.
 *  Space: O(n) recursion + output.
 *  Pattern: backtracking with dedupe-by-position trick.
 * ============================================================ */
