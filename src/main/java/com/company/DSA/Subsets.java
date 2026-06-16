package com.company.DSA;

import java.util.*;

/**
 * LeetCode #78 - Subsets
 * Backtrack: at each index, two choices: include or skip.
 * Time: O(n * 2^n)  Space: O(n) recursion + output
 */
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
