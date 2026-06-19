package com.company.DSA.binarysearch;

/* ============================================================
 *  LeetCode #704 — Binary Search (template)
 * ============================================================
 *  PROBLEM
 *  -------
 *  Standard binary search on a sorted ascending int array. Return index or -1.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[-1,0,3,5,9,12], target=9 → 4
 *  Ex2: nums=[-1,0,3,5,9,12], target=2 → -1
 *  Ex3: nums=[5], target=5 → 0
 *
 *  CONSTRAINTS:  1 <= n <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Closed interval [l, r]. mid = l + (r-l)/2.
 * ============================================================ */
public class BinarySearch {

    public int search(final int[] sortedNumbers, final int target) {
        int leftCursor  = 0;
        int rightCursor = sortedNumbers.length - 1;

        while (leftCursor <= rightCursor) {
            final int midIndex = leftCursor + (rightCursor - leftCursor) / 2;
            final int midValue = sortedNumbers[midIndex];

            if (midValue == target) {
                return midIndex;
            }
            if (midValue < target) {
                leftCursor = midIndex + 1;
            } else {
                rightCursor = midIndex - 1;
            }
        }
        return -1;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Closed interval template. Always shrinks → can't infinite loop.
 *
 *  Complexity: Time O(log n), Space O(1).
 *  Pattern: foundation for all search-on-sorted-data problems.
 * ============================================================ */
