package com.company.DSA;

import java.util.*;

/**
 * LeetCode #47 - Permutations II (with dups)
 * Sort. In branch, skip nums[i]==nums[i-1] if previous not used.
 * Time: O(n * n!)  Space: O(n)
 */
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
