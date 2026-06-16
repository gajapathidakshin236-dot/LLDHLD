package com.company.DSA;

/**
 * LeetCode #1493 - Longest Subarray of 1s After Deleting One Element
 * Sliding window allowing at most one 0; answer = window length - 1 (we must delete one).
 * Time: O(n)  Space: O(1)
 */
public class LongestSubarrayDelete1 {
    public int longestSubarray(int[] nums) {
        int l = 0, zero = 0, best = 0;
        for (int r = 0; r < nums.length; r++) {
            if (nums[r] == 0) zero++;
            while (zero > 1) if (nums[l++] == 0) zero--;
            best = Math.max(best, r - l);
        }
        return best;
    }
}
