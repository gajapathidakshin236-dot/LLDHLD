package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #46 — Permutations
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given an array of distinct integers, return all n! permutations.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,3] → [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 *  Ex2: [0,1]   → [[0,1],[1,0]]
 *  Ex3: [1]     → [[1]]
 *
 *  CONSTRAINTS:  1 <= n <= 6; values distinct.
 *
 *  HINTS
 *  -----
 *   1. Backtrack with a used[] boolean array (or by swap-in-place).
 *   2. Snapshot when current size equals n.
 * ============================================================ */
public class Permutations {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        bt(nums, new boolean[nums.length], new ArrayList<>(), res);
        return res;
    }
    private void bt(int[] nums, boolean[] used, List<Integer> cur, List<List<Integer>> res) {
        if (cur.size() == nums.length) { res.add(new ArrayList<>(cur)); return; }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            used[i] = true; cur.add(nums[i]);
            bt(nums, used, cur, res);
            used[i] = false; cur.remove(cur.size() - 1);
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Classic backtracking. used[] tracks which elements are already in the
 *  current partial permutation. Snapshot when length == n; backtrack by
 *  un-marking on the way back up.
 *
 *  Swap-in-place alt:
 *    Iteratively swap nums[i] with nums[start..n-1]; recurse with start+1;
 *    swap back. Saves the used array.
 *
 *  Complexity: Time O(n * n!), Space O(n) recursion + O(n!) output.
 *  Edge cases: single element → [[x]].
 *  Pattern: backtracking with state mutation + undo.
 * ============================================================ */
