package com.company.DSA;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class TwoSumSolution {
    private TwoSumSolution() {
    }

    /**
     * Returns indices of the 2 numbers such that they add up to target.
     * If no solution exists, returns {-1, -1}.
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer,  Integer> seen = new HashMap<>(); // value -> index
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            int need = target - nums[i];
            Integer j = seen.get(need);
            if (j != null) {
                return new int[]{j, i};
            }
            seen.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }

    public static void main(String[] args) {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        int[] ans = twoSum(nums, target);
        System.out.println(ans[0] + " " + ans[1]);
    }
}

