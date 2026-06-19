package com.company.DSA.binarysearch;

/* ============================================================
 *  LeetCode #34 — Find First and Last Position of Element in Sorted Array
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return [first, last] indices of target, or [-1, -1].
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[5,7,7,8,8,10], target=8 → [3,4]
 *  Ex2: nums=[5,7,7,8,8,10], target=6 → [-1,-1]
 *  Ex3: nums=[], target=0 → [-1,-1]
 *
 *  CONSTRAINTS:  0 <= n <= 10^5; sorted ascending.
 *
 *  HINTS
 *  -----
 *   1. Lower bound and upper bound binary searches.
 * ============================================================ */
public class FindFirstLastPosition {

    public int[] searchRange(final int[] sortedNumbers, final int target) {
        final int lowerBoundIndex = findLowerBound(sortedNumbers, target);

        if (lowerBoundIndex == sortedNumbers.length || sortedNumbers[lowerBoundIndex] != target) {
            return new int[] { -1, -1 };
        }

        final int upperBoundIndex = findUpperBound(sortedNumbers, target) - 1;
        return new int[] { lowerBoundIndex, upperBoundIndex };
    }

    private int findLowerBound(final int[] sortedNumbers, final int target) {
        int leftCursor  = 0;
        int rightCursor = sortedNumbers.length;

        while (leftCursor < rightCursor) {
            final int midIndex = leftCursor + (rightCursor - leftCursor) / 2;
            if (sortedNumbers[midIndex] >= target) {
                rightCursor = midIndex;
            } else {
                leftCursor = midIndex + 1;
            }
        }
        return leftCursor;
    }

    private int findUpperBound(final int[] sortedNumbers, final int target) {
        int leftCursor  = 0;
        int rightCursor = sortedNumbers.length;

        while (leftCursor < rightCursor) {
            final int midIndex = leftCursor + (rightCursor - leftCursor) / 2;
            if (sortedNumbers[midIndex] > target) {
                rightCursor = midIndex;
            } else {
                leftCursor = midIndex + 1;
            }
        }
        return leftCursor;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two binary searches: lower bound (first >= target) and upper bound
 *  (first > target). Last = upper - 1.
 *
 *  Complexity: Time O(log n), Space O(1).
 *  Pattern: lower/upper bound.
 * ============================================================ */
