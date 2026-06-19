package com.company.DSA.slidingwindow;

/* ============================================================
 *  LeetCode #1493 — Longest Subarray of 1's After Deleting One Element
 * ============================================================
 *  PROBLEM
 *  -------
 *  Delete EXACTLY one element. Return the length of the longest 1-only subarray.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,1,0,1] → 3
 *  Ex2: [0,1,1,1,0,1,1,0,1] → 5
 *  Ex3: [1,1,1] → 2
 *  Ex4: [0,0,0] → 0
 *
 *  CONSTRAINTS:  1 <= n <= 10^5; nums[i] in {0,1}.
 *
 *  HINTS
 *  -----
 *   1. Sliding window with AT MOST one zero.
 *   2. Best = window length - 1 (we must delete one element).
 * ============================================================ */
public class LongestSubarrayDelete1 {

    public int longestSubarray(final int[] binary) {
        int leftCursor    = 0;
        int zerosInWindow = 0;
        int bestLength    = 0;

        for (int rightCursor = 0; rightCursor < binary.length; rightCursor++) {
            if (binary[rightCursor] == 0) {
                zerosInWindow++;
            }
            while (zerosInWindow > 1) {
                if (binary[leftCursor] == 0) {
                    zerosInWindow--;
                }
                leftCursor++;
            }
            bestLength = Math.max(bestLength, rightCursor - leftCursor);
        }
        return bestLength;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Sliding window allowing one zero. Best = window length minus 1, since we
 *  must delete one element.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: variable window with constraint on bad-element count.
 * ============================================================ */
