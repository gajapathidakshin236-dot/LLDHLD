package com.company.DSA.binarysearch;

/* ============================================================
 *  LeetCode #153 — Find Minimum in Rotated Sorted Array
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return min in a rotated sorted array. O(log n).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[3,4,5,1,2]     → 1
 *  Ex2: nums=[4,5,6,7,0,1,2] → 0
 *  Ex3: nums=[11,13,15,17]   → 11
 *
 *  CONSTRAINTS:  n in [1, 5000]; unique values.
 *
 *  HINTS
 *  -----
 *   1. Compare nums[mid] with nums[right]. Move accordingly.
 * ============================================================ */
public class FindMinRotatedSorted {

    public int findMin(final int[] rotatedSortedNumbers) {
        int leftCursor  = 0;
        int rightCursor = rotatedSortedNumbers.length - 1;

        while (leftCursor < rightCursor) {
            final int midIndex = leftCursor + (rightCursor - leftCursor) / 2;

            if (rotatedSortedNumbers[midIndex] > rotatedSortedNumbers[rightCursor]) {
                leftCursor = midIndex + 1;
            } else {
                rightCursor = midIndex;
            }
        }
        return rotatedSortedNumbers[leftCursor];
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Compare midValue with rightmost element. Greater → minimum strictly right.
 *  Else minimum is at mid or to its left.
 *
 *  Complexity: Time O(log n), Space O(1).
 *  Pattern: half-eliminate via right-anchor.
 * ============================================================ */
