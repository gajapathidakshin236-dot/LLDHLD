package com.company.DSA;

/**
 * LeetCode #35 - Search Insert Position
 * Lower bound: first index where nums[i] >= target.
 * Time: O(log n)  Space: O(1)
 */
public class SearchInsertPosition {
    public int searchInsert(int[] nums, int target) {
        int l = 0, r = nums.length;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (nums[m] < target) l = m + 1;
            else r = m;
        }
        return l;
    }
}
