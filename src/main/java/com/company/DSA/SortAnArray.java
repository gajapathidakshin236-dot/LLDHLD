package com.company.DSA;

/**
 * LeetCode #912 - Sort an Array (mergesort, stable)
 * Time: O(n log n)  Space: O(n)
 */
public class SortAnArray {
    public int[] sortArray(int[] nums) {
        int[] aux = new int[nums.length];
        sort(nums, aux, 0, nums.length - 1);
        return nums;
    }
    private void sort(int[] a, int[] aux, int l, int r) {
        if (l >= r) return;
        int m = l + (r - l) / 2;
        sort(a, aux, l, m); sort(a, aux, m + 1, r);
        merge(a, aux, l, m, r);
    }
    private void merge(int[] a, int[] aux, int l, int m, int r) {
        for (int i = l; i <= r; i++) aux[i] = a[i];
        int i = l, j = m + 1, k = l;
        while (i <= m && j <= r) a[k++] = aux[i] <= aux[j] ? aux[i++] : aux[j++];
        while (i <= m) a[k++] = aux[i++];
    }
}
