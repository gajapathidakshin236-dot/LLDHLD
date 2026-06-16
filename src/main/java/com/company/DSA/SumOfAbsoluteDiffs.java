package com.company.DSA;

import java.util.*;

/**
 * LeetCode #1685 - Sum of Absolute Differences in a Sorted Array
 * For sorted a: at index i, sum = i*a[i] - prefL + sufR - (n-1-i)*a[i].
 * Time: O(n)  Space: O(n)
 */
public class SumOfAbsoluteDiffs {
    public int[] getSumAbsoluteDifferences(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        long total = 0;
        for (int v : nums) total += v;
        long left = 0;
        for (int i = 0; i < n; i++) {
            long right = total - left - nums[i];
            res[i] = (int) (((long) i * nums[i] - left) + (right - (long) (n - 1 - i) * nums[i]));
            left += nums[i];
        }
        return res;
    }
}
