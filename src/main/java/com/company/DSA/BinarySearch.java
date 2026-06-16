package com.company.DSA;

/**
 * LeetCode #704 - Binary Search (classic template)
 * Closed [l, r] range; mid = l + (r-l)/2 avoids overflow.
 * Time: O(log n)  Space: O(1)
 */
public class BinarySearch {
    public int search(int[] nums, int target) {
        int l = 0, r = nums.length - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (nums[m] == target) return m;
            if (nums[m] < target) l = m + 1;
            else r = m - 1;
        }
        return -1;
    }
}
