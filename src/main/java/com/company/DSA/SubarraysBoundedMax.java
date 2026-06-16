package com.company.DSA;

/**
 * LeetCode #795 - Number of Subarrays with Bounded Maximum
 * count(<=R) - count(<=L-1). count_<=k: walk; reset count after element > k.
 * Time: O(n)  Space: O(1)
 */
public class SubarraysBoundedMax {
    public int numSubarrayBoundedMax(int[] nums, int left, int right) {
        return count(nums, right) - count(nums, left - 1);
    }
    private int count(int[] a, int k) {
        int res = 0, cur = 0;
        for (int x : a) {
            if (x <= k) { cur++; res += cur; }
            else cur = 0;
        }
        return res;
    }
}
