package com.company.DSA;

import java.util.*;

/**
 * LeetCode #46 - Permutations
 * Backtrack with a `used[]` boolean array.
 * Time: O(n * n!)  Space: O(n)
 */
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
