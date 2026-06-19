package com.company.DSA.slidingwindow;

/* ============================================================
 *  LeetCode #209 — Minimum Size Subarray Sum
 * ============================================================
 *  PROBLEM
 *  -------
 *  Minimum length of a contiguous subarray with sum >= target.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: target=7, nums=[2,3,1,2,4,3] → 2
 *  Ex2: target=4, nums=[1,4,4]       → 1
 *  Ex3: target=11, nums=[1,1,1,1,1,1,1,1] → 0
 *
 *  CONSTRAINTS:  1 <= n <= 10^5; 1 <= val <= 10^4; 1 <= target <= 10^9.
 *
 *  HINTS
 *  -----
 *   1. All positive → monotonic window works.
 *   2. Expand right; while sum >= target, shrink left and update best.
 * ============================================================ */
public class MinSizeSubarraySum {

    public int minSubArrayLen(final int targetSum, final int[] numbers) {
        int leftCursor       = 0;
        int currentSum       = 0;
        int bestLength       = Integer.MAX_VALUE;

        for (int rightCursor = 0; rightCursor < numbers.length; rightCursor++) {
            currentSum += numbers[rightCursor];
            while (currentSum >= targetSum) {
                bestLength = Math.min(bestLength, rightCursor - leftCursor + 1);
                currentSum -= numbers[leftCursor++];
            }
        }
        return bestLength == Integer.MAX_VALUE ? 0 : bestLength;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Sliding window with monotonic shrink. Positive values make sum monotone
 *  so window can grow and shrink predictably.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: shrinking window for "minimum size satisfying constraint."
 * ============================================================ */
