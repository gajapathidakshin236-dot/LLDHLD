package com.company.DSA;

import java.util.*;

/**
 * LeetCode #90 - Subsets II (input may have duplicates)
 * Sort first; in each branch skip subsequent equal elements to dedupe.
 * Time: O(n * 2^n)  Space: O(n)
 */
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
