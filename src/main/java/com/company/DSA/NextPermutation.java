package com.company.DSA;

/**
 * LeetCode #31 - Next Permutation
 * 1) find pivot i: largest i with a[i] < a[i+1].
 * 2) if exists, swap with smallest a[j]>a[i] in suffix.
 * 3) reverse suffix starting i+1.
 * Time: O(n)  Space: O(1)
 */
public class NextPermutation {
    public void nextPermutation(int[] nums) {
        int n = nums.length, i = n - 2;
        while (i >= 0 && nums[i] >= nums[i + 1]) i--;
        if (i >= 0) {
            int j = n - 1;
            while (nums[j] <= nums[i]) j--;
            swap(nums, i, j);
        }
        reverse(nums, i + 1, n - 1);
    }
    private void swap(int[] a, int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }
    private void reverse(int[] a, int l, int r) { while (l < r) swap(a, l++, r--); }
}
