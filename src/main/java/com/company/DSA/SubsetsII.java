package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #90 — Subsets II
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given an array with possible duplicates, return all unique subsets.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,2] → [[],[1],[1,2],[1,2,2],[2],[2,2]]
 *  Ex2: [0]     → [[],[0]]
 *  Ex3: [1,1,1] → [[],[1],[1,1],[1,1,1]]
 *
 *  CONSTRAINTS:  1 <= n <= 10; -10 <= val <= 10.
 *
 *  HINTS
 *  -----
 *   1. Sort the array so duplicates are adjacent.
 *   2. At each recursion LEVEL, skip duplicates after the first selection.
 *   3. Pattern: `if (j > start && nums[j] == nums[j-1]) continue;`
 * ============================================================ */
public class SubsetsII {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        bt(nums, 0, new ArrayList<>(), res);
        return res;
    }
    private void bt(int[] nums, int i, List<Integer> cur, List<List<Integer>> res) {
        res.add(new ArrayList<>(cur));
        for (int j = i; j < nums.length; j++) {
            if (j > i && nums[j] == nums[j - 1]) continue;
            cur.add(nums[j]);
            bt(nums, j + 1, cur, res);
            cur.remove(cur.size() - 1);
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Sort first → duplicates become adjacent. In the for-loop over choices at a
 *  given level, when we already chose nums[j-1] at this position, choosing
 *  nums[j] (equal) again would yield the same subset → skip.
 *  Note: `j > i` not `j > 0`. Within the same recursion level, only skip equals.
 *  Across levels, we still want to include duplicates (e.g., [2,2]).
 *
 *  Complexity: Time O(n * 2^n), Space O(n) stack + O(n * 2^n) output.
 *  Edge cases: all unique → behaves like #78.
 *  Pattern: "sort + skip equals at same level" — the standard dedupe pattern.
 *           Same trick: #40 Combination Sum II, #47 Permutations II.
 * ============================================================ */
