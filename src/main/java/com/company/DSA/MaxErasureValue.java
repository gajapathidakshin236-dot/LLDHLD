package com.company.DSA;

import java.util.*;

/**
 * LeetCode #1695 - Maximum Erasure Value (longest sum of unique-element subarray)
 * Sliding window with HashSet; shrink while duplicate of nums[r] present.
 * Time: O(n)  Space: O(n)
 */
public class MaxErasureValue {
    public int maximumUniqueSubarray(int[] nums) {
        Set<Integer> set = new HashSet<>();
        int l = 0, sum = 0, best = 0;
        for (int r = 0; r < nums.length; r++) {
            while (set.contains(nums[r])) { set.remove(nums[l]); sum -= nums[l++]; }
            set.add(nums[r]);
            sum += nums[r];
            best = Math.max(best, sum);
        }
        return best;
    }
}
