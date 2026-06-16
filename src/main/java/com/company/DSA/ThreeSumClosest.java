package com.company.DSA;

import java.util.*;

/**
 * LeetCode #16 - 3Sum Closest
 * Sort. Fix i; two pointers l, r minimize |sum - target|.
 * Time: O(n^2)  Space: O(1)
 */
public class ThreeSumClosest {
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int best = nums[0] + nums[1] + nums[2];
        for (int i = 0; i < nums.length - 2; i++) {
            int l = i + 1, r = nums.length - 1;
            while (l < r) {
                int sum = nums[i] + nums[l] + nums[r];
                if (Math.abs(sum - target) < Math.abs(best - target)) best = sum;
                if (sum < target) l++; else r--;
            }
        }
        return best;
    }
}
