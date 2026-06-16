package com.company.DSA;

import java.util.*;

/**
 * LeetCode #974 - Subarray Sums Divisible by K
 * Count prefix sums by remainder. Pair counts: each pair of same remainder = valid subarray.
 * Handle negative mods: ((sum % k) + k) % k.
 * Time: O(n)  Space: O(k)
 */
public class SubarraySumsDivisibleByK {
    public int subarraysDivByK(int[] nums, int k) {
        int[] cnt = new int[k];
        cnt[0] = 1;
        int sum = 0, ans = 0;
        for (int x : nums) {
            sum += x;
            int r = ((sum % k) + k) % k;
            ans += cnt[r]++;
        }
        return ans;
    }
}
