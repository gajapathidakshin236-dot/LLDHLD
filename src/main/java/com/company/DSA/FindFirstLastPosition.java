package com.company.DSA;

/**
 * LeetCode #34 - Find First and Last Position of Element in Sorted Array
 * Two binary searches: leftmost & rightmost. Standard lower/upper bound.
 * Time: O(log n)  Space: O(1)
 */
public class FindFirstLastPosition {
    public int[] searchRange(int[] nums, int target) {
        int lo = bound(nums, target, true);
        if (lo == nums.length || nums[lo] != target) return new int[]{-1, -1};
        int hi = bound(nums, target, false) - 1;
        return new int[]{lo, hi};
    }
    private int bound(int[] nums, int t, boolean lower) {
        int l = 0, r = nums.length;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (nums[m] > t || (lower && nums[m] == t)) r = m;
            else l = m + 1;
        }
        return l;
    }
}
