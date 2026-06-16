package com.company.DSA;

/* ============================================================
 *  LeetCode #912 — Sort an Array (don't use library sort)
 * ============================================================
 *  PROBLEM
 *  -------
 *  Sort the integer array in O(n log n) time with O(log n) auxiliary space
 *  if possible.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [5,2,3,1] → [1,2,3,5]
 *  Ex2: [5,1,1,2,0,0] → [0,0,1,1,2,5]
 *
 *  CONSTRAINTS:  1 <= n <= 5*10^4;  -5*10^4 <= val <= 5*10^4.
 *
 *  HINTS
 *  -----
 *   1. Quicksort risk: O(n^2) worst — randomize pivot.
 *   2. Mergesort: deterministic O(n log n) but O(n) aux space.
 *   3. Heap sort: O(n log n) in-place but slower constants.
 * ============================================================ */
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

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Top-down mergesort. Recursively sort halves, then merge using a shared
 *  auxiliary buffer to avoid re-allocations.
 *
 *  Why mergesort:
 *    Predictable O(n log n) regardless of input; stable; easy to reason about.
 *
 *  Complexity: Time O(n log n), Space O(n).
 *  Pattern: divide & conquer. Foundation for problems that ask "implement sort"
 *           or use merge step as a primitive (#315, #493).
 * ============================================================ */
