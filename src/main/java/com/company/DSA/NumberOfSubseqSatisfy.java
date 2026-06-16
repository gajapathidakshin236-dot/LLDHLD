package com.company.DSA;

import java.util.*;

/**
 * LeetCode #1498 - Number of Subsequences That Satisfy the Given Sum Condition
 * Sort. Two pointers; if a[l]+a[r] <= target, all subsets of a[l+1..r] valid -> += 2^(r-l).
 * Precompute powers mod 1e9+7.
 * Time: O(n log n)  Space: O(n)
 */
public class NumberOfSubseqSatisfy {
    public int numSubseq(int[] nums, int target) {
        int MOD = 1_000_000_007, n = nums.length;
        Arrays.sort(nums);
        int[] pw = new int[n];
        pw[0] = 1;
        for (int i = 1; i < n; i++) pw[i] = pw[i - 1] * 2 % MOD;
        int l = 0, r = n - 1, ans = 0;
        while (l <= r) {
            if (nums[l] + nums[r] <= target) { ans = (ans + pw[r - l]) % MOD; l++; }
            else r--;
        }
        return ans;
    }
}
