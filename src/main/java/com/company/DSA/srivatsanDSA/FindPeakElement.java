package com.company.DSA.srivatsanDSA;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 73
 *  Problem: Find Peak Element
 *  A peak satisfies nums[i] > nums[i-1] and nums[i] > nums[i+1].
 *  nums[-1] = nums[n] = -infinity. O(log n).
 *
 *  APPROACH (from notes):
 *    Binary search on the slope.
 *    while (l < r):
 *      mid = (l + r) / 2
 *      if nums[mid] > nums[mid + 1] → r = mid  (peak is left, including mid)
 *      else                          → l = mid + 1  (peak is right)
 *    return l
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
