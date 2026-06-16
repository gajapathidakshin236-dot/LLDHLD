package com.company.DSA;

/**
 * LeetCode #1658 - Min Ops to Reduce X to Zero
 * Equivalent: longest subarray with sum = total - x. Sliding window.
 * Time: O(n)  Space: O(1)
 */
public class MinOpsReduceXToZero {
    public int minOperations(int[] nums, int x) {
        int total = 0;
        for (int v : nums) total += v;
        int need = total - x;
        if (need < 0) return -1;
        int l = 0, sum = 0, best = -1;
        for (int r = 0; r < nums.length; r++) {
            sum += nums[r];
            while (sum > need) sum -= nums[l++];
            if (sum == need) best = Math.max(best, r - l + 1);
        }
        return best < 0 ? -1 : nums.length - best;
    }
}
