package com.company.DSA;

/* ============================================================
 *  LeetCode #31 — Next Permutation
 * ============================================================
 *  PROBLEM
 *  -------
 *  Rearrange nums into the lexicographically NEXT greater permutation.
 *  If no such arrangement (sorted descending), wrap around to smallest (ascending).
 *  In place; constant extra memory.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,3] → [1,3,2]
 *  Ex2: [3,2,1] → [1,2,3]   (wrap)
 *  Ex3: [1,1,5] → [1,5,1]
 *  Ex4: [1,3,2] → [2,1,3]
 *  Ex5: [2,3,1] → [3,1,2]
 *
 *  CONSTRAINTS:  1 <= n <= 100; 0 <= val <= 100.
 *
 *  HINTS
 *  -----
 *   1. Find the largest i such that nums[i] < nums[i+1] — that's the pivot.
 *   2. If no such i → reverse entire array (we were at the largest perm).
 *   3. Else find smallest val > nums[i] in suffix; swap; reverse suffix.
 * ============================================================ */
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

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Standard "next-permutation" algorithm:
 *    Step 1: Find pivot i where suffix [i+1..end] is descending. The pivot is
 *            the largest position where nums[i] < nums[i+1].
 *    Step 2: If no pivot, the array is the largest perm; reverse → smallest.
 *    Step 3: Find smallest j > i with nums[j] > nums[i] (rightmost such j due
 *            to descending suffix). Swap.
 *    Step 4: Reverse suffix (now ascending) — that's the smallest greater perm.
 *
 *  Why this is the "next" perm:
 *    Increasing the pivot to the next possible value, then making the suffix
 *    as small as possible (sorted ascending) → minimal increment.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: combinatorial generation. Same algorithm underlies C++ std::next_permutation.
 * ============================================================ */
