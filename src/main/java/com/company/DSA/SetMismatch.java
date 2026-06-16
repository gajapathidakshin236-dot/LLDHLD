package com.company.DSA;

/**
 * LeetCode #645 - Set Mismatch
 * Index-marking: negate nums[abs(v)-1]; if already negative -> duplicate. Then find positive index = missing.
 * Time: O(n)  Space: O(1)
 */
public class SetMismatch {
    public int[] findErrorNums(int[] nums) {
        int dup = -1, missing = -1;
        for (int v : nums) {
            int i = Math.abs(v) - 1;
            if (nums[i] < 0) dup = i + 1;
            else nums[i] = -nums[i];
        }
        for (int i = 0; i < nums.length; i++) if (nums[i] > 0) missing = i + 1;
        return new int[]{dup, missing};
    }
}
