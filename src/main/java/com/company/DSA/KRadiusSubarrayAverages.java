package com.company.DSA;

/**
 * LeetCode #2090 - K Radius Subarray Averages
 * Sliding window of size 2k+1; for i in [k, n-1-k] write window sum / (2k+1).
 * Use long sum to avoid overflow.
 * Time: O(n)  Space: O(n)
 */
public class KRadiusSubarrayAverages {
    public int[] getAverages(int[] nums, int k) {
        int n = nums.length, w = 2 * k + 1;
        int[] res = new int[n];
        java.util.Arrays.fill(res, -1);
        if (w > n) return res;
        long sum = 0;
        for (int i = 0; i < w; i++) sum += nums[i];
        res[k] = (int) (sum / w);
        for (int i = w; i < n; i++) {
            sum += nums[i] - nums[i - w];
            res[i - k] = (int) (sum / w);
        }
        return res;
    }
}
