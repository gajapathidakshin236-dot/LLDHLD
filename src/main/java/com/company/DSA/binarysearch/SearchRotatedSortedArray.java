package com.company.DSA.binarysearch;

/* ============================================================
 *  LeetCode #33 — Search in Rotated Sorted Array
 * ============================================================
 *  PROBLEM
 *  -------
 *  Rotated sorted array; find target index or -1 in O(log n).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[4,5,6,7,0,1,2], target=0 → 4
 *  Ex2: nums=[4,5,6,7,0,1,2], target=3 → -1
 *  Ex3: nums=[5,1,3], target=3 → 2
 *
 *  CONSTRAINTS:  1 <= n <= 5000; values unique.
 *
 *  HINTS
 *  -----
 *   1. One half is always sorted; go to whichever contains target.
 * ============================================================ */
public class SearchRotatedSortedArray {

    public int search(final int[] rotatedSortedNumbers, final int target) {
        int leftCursor  = 0;
        int rightCursor = rotatedSortedNumbers.length - 1;

        while (leftCursor <= rightCursor) {
            final int midIndex = leftCursor + (rightCursor - leftCursor) / 2;
            final int midValue = rotatedSortedNumbers[midIndex];

            if (midValue == target) {
                return midIndex;
            }

            final boolean leftHalfSorted = rotatedSortedNumbers[leftCursor] <= midValue;
            if (leftHalfSorted) {
                final boolean targetInLeftHalf = rotatedSortedNumbers[leftCursor] <= target && target < midValue;
                if (targetInLeftHalf) {
                    rightCursor = midIndex - 1;
                } else {
                    leftCursor  = midIndex + 1;
                }
            } else {
                final boolean targetInRightHalf = midValue < target && target <= rotatedSortedNumbers[rightCursor];
                if (targetInRightHalf) {
                    leftCursor  = midIndex + 1;
                } else {
                    rightCursor = midIndex - 1;
                }
            }
        }
        return -1;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Modified binary search using "one half is always sorted." Decide via
 *  nums[l] vs nums[m] which half is sorted, then check if target lies inside.
 *
 *  Complexity: Time O(log n), Space O(1).
 *  Pattern: binary search with rotation invariant.
 * ============================================================ */
