package com.company.DSA.binarysearch;

/* ============================================================
 *  LeetCode #35 — Search Insert Position
 * ============================================================
 *  PROBLEM
 *  -------
 *  Find target index in sorted array, or insertion position if absent.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[1,3,5,6], target=5 → 2
 *  Ex2: nums=[1,3,5,6], target=2 → 1
 *  Ex3: nums=[1,3,5,6], target=7 → 4
 *  Ex4: nums=[1,3,5,6], target=0 → 0
 *
 *  CONSTRAINTS:  1 <= n <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Lower bound: first index with nums[i] >= target.
 * ============================================================ */
public class SearchInsertPosition {

    public int searchInsert(final int[] sortedNumbers, final int target) {
        int leftCursor  = 0;
        int rightCursor = sortedNumbers.length;

        while (leftCursor < rightCursor) {
            final int midIndex = leftCursor + (rightCursor - leftCursor) / 2;
            if (sortedNumbers[midIndex] < target) {
                leftCursor = midIndex + 1;
            } else {
                rightCursor = midIndex;
            }
        }
        return leftCursor;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Lower bound: r = nums.length so answer may be == n when target > all.
 *
 *  Complexity: Time O(log n), Space O(1).
 *  Pattern: lower-bound binary search.
 * ============================================================ */
