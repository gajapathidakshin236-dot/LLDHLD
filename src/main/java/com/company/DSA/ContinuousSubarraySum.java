package com.company.DSA;

import java.util.*;

/**
 * LeetCode #523 - Continuous Subarray Sum
 * Track first index where remainder appeared. If same remainder seen again and gap >= 2 -> true.
 * Time: O(n)  Space: O(min(n, k))
 */
public class ContinuousSubarraySum {
    public boolean checkSubarraySum(int[] nums, int k) {
        Map<Integer,Integer> first = new HashMap<>();
        first.put(0, -1);
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            int r = sum % k;
            if (r < 0) r += k;
            if (first.containsKey(r)) { if (i - first.get(r) >= 2) return true; }
            else first.put(r, i);
        }
        return false;
    }
}
