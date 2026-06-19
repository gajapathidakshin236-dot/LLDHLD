package com.company.DSA.binarysearch;

/* ============================================================
 *  LeetCode #162 — Find Peak Element
 * ============================================================
 *  PROBLEM
 *  -------
 *  Find ANY peak in O(log n). nums[-1] = nums[n] = -infinity.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[1,2,3,1] → 2
 *  Ex2: nums=[1,2,1,3,5,6,4] → 1 or 5
 *  Ex3: nums=[1] → 0
 *  Ex4: nums=[1,2] → 1
 *
 *  CONSTRAINTS:  1 <= n <= 1000.
 *
 *  HINTS
 *  -----
 *   1. Climb toward the higher neighbor.
 * ============================================================ */
public class FindPeakElement {

    public int findPeakElement(final int[] numbers) {
        int leftCursor  = 0;
        int rightCursor = numbers.length - 1;

        while (leftCursor < rightCursor) {
            final int midIndex = leftCursor + (rightCursor - leftCursor) / 2;

            if (numbers[midIndex] > numbers[midIndex + 1]) {
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
 *  Walk uphill: if nums[m] > nums[m+1] peak lies in [l..m], else (m+1..r].
 *  Strictly distinct neighbors (per constraints) guarantee termination.
 *
 *  Complexity: Time O(log n), Space O(1).
 *  Pattern: binary search on slope direction.
 * ============================================================ */
