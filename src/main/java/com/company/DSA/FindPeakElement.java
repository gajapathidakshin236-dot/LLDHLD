package com.company.DSA;

/**
 * LeetCode #162 - Find Peak Element
 * If nums[m] > nums[m+1] peak is on left (incl m), else on right.
 * Time: O(log n)  Space: O(1)
 */
public class FindPeakElement {
    public int findPeakElement(int[] nums) {
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (nums[m] > nums[m + 1]) r = m;
            else l = m + 1;
        }
        return l;
    }
}
