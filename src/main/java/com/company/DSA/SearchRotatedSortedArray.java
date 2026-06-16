package com.company.DSA;

/**
 * LeetCode #33 - Search in Rotated Sorted Array
 * At each mid, one half [l..m] or [m..r] is sorted. If target lies in the sorted half, go there.
 * Time: O(log n)  Space: O(1)
 */
public class SearchRotatedSortedArray {
    public int search(int[] nums, int t) {
        int l = 0, r = nums.length - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (nums[m] == t) return m;
            if (nums[l] <= nums[m]) { // left half sorted
                if (nums[l] <= t && t < nums[m]) r = m - 1;
                else l = m + 1;
            } else {
                if (nums[m] < t && t <= nums[r]) l = m + 1;
                else r = m - 1;
            }
        }
        return -1;
    }
}
