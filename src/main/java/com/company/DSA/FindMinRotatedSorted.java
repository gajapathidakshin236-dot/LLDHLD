package com.company.DSA;

/**
 * LeetCode #153 - Find Minimum in Rotated Sorted Array
 * Compare nums[m] with nums[r]. If nums[m] > nums[r] minimum is right; else left side (incl m).
 * Time: O(log n)  Space: O(1)
 */
public class FindMinRotatedSorted {
    public int findMin(int[] nums) {
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (nums[m] > nums[r]) l = m + 1;
            else r = m;
        }
        return nums[l];
    }
}
