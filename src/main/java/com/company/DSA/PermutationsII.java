package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #47 — Permutations II (with duplicates)
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given a collection of numbers that might contain duplicates, return all
 *  possible UNIQUE permutations.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,1,2] → [[1,1,2],[1,2,1],[2,1,1]]
 *  Ex2: [1,2,3] → 6 permutations (no dups)
 *  Ex3: [2,2,2] → [[2,2,2]]
 *
 *  CONSTRAINTS:  1 <= n <= 8.
 *
 *  HINTS
 *  -----
 *   1. Sort so duplicates are adjacent.
 *   2. Skip rule: when nums[i] == nums[i-1] and i-1 is NOT used → skip.
 *      Ensures each "first identical position" is the canonical choice.
 * ============================================================ */
public class PermutationsII {
    public List<List<Integer>> permuteUnique(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        bt(nums, new boolean[nums.length], new ArrayList<>(), res);
        return res;
    }
    private void bt(int[] nums, boolean[] used, List<Integer> cur, List<List<Integer>> res) {
        if (cur.size() == nums.length) { res.add(new ArrayList<>(cur)); return; }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) continue;
            used[i] = true; cur.add(nums[i]);
            bt(nums, used, cur, res);
            used[i] = false; cur.remove(cur.size() - 1);
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Backtracking + dedupe rule.
 *  After sort, equal values are adjacent. To avoid the same multiset placed
 *  in the same positions in different orders of choice, we require:
 *    For equal values, the previous occurrence (i-1) MUST have been used
 *    before the current one (i). The negation gives us the skip condition.
 *
 *  Equivalent: "fix a canonical ordering for equals" — left-to-right.
 *
 *  Complexity: Time O(n * n!) worst, fewer with many dups.
 *  Space: O(n) recursion + O(unique!) output.
 *  Pattern: backtracking + canonical-order dedupe.
 * ============================================================ */
