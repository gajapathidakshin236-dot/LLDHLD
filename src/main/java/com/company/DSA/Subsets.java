package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #78 — Subsets
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given a distinct integer array nums, return all possible subsets (power set).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,3] → [[],[1],[2],[3],[1,2],[1,3],[2,3],[1,2,3]]
 *  Ex2: [0]     → [[],[0]]
 *  Ex3: [1,2]   → [[],[1],[2],[1,2]]
 *
 *  CONSTRAINTS:  1 <= n <= 10; values unique; -10 <= val <= 10.
 *
 *  HINTS
 *  -----
 *   1. Each element has 2 choices: include or skip. Total = 2^n subsets.
 *   2. Recurse on (index, current). Add snapshot when index hits end.
 *   3. Alternative: bitmask, where bit i in mask says include nums[i].
 * ============================================================ */
public class Subsets {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        bt(nums, 0, new ArrayList<>(), res);
        return res;
    }
    private void bt(int[] nums, int i, List<Integer> cur, List<List<Integer>> res) {
        if (i == nums.length) { res.add(new ArrayList<>(cur)); return; }
        cur.add(nums[i]); bt(nums, i + 1, cur, res); cur.remove(cur.size() - 1);
        bt(nums, i + 1, cur, res);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Decision-tree recursion: at each index, branch into INCLUDE and SKIP.
 *  Snapshot when index reaches array length.
 *
 *  Iterative alt:
 *    Start with [[]]. For each x in nums, copy every existing subset and
 *    append x to the copy → doubles the set each step.
 *
 *  Bitmask alt:
 *    for mask in 0..(1<<n) - 1, include nums[i] if (mask >> i) & 1.
 *
 *  Complexity: Time O(n * 2^n), Space O(n) recursion + O(n * 2^n) output.
 *  Edge cases: single element → [[],[x]]; empty array → [[]].
 *  Pattern: include/exclude backtracking — foundation for combination/permutation problems.
 * ============================================================ */
