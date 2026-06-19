package com.company.DSA.slidingwindow;

/* ============================================================
 *  LeetCode #1208 — Get Equal Substrings Within Budget
 * ============================================================
 *  PROBLEM
 *  -------
 *  Longest substring s.t. total |s[i]-t[i]| <= maxCost.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: s="abcd", t="bcdf", maxCost=3 → 3
 *  Ex2: s="abcd", t="cdef", maxCost=3 → 1
 *  Ex3: s="abcd", t="acde", maxCost=0 → 1
 *
 *  CONSTRAINTS:  1 <= len <= 10^5; 0 <= maxCost <= 10^6.
 *
 *  HINTS
 *  -----
 *   1. Sliding window over conversion costs; shrink while sum > maxCost.
 * ============================================================ */
public class EqualSubstringsBudget {

    public int equalSubstring(final String source, final String target, final int maxCost) {
        int leftCursor      = 0;
        int currentCostSum  = 0;
        int bestLength      = 0;

        for (int rightCursor = 0; rightCursor < source.length(); rightCursor++) {
            currentCostSum += Math.abs(source.charAt(rightCursor) - target.charAt(rightCursor));

            while (currentCostSum > maxCost) {
                currentCostSum -= Math.abs(source.charAt(leftCursor) - target.charAt(leftCursor));
                leftCursor++;
            }
            bestLength = Math.max(bestLength, rightCursor - leftCursor + 1);
        }
        return bestLength;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Treat the per-index conversion costs as an array; find the longest subarray
 *  whose sum <= budget. Sliding window works because costs are non-negative.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: "max window with sum <= K."
 * ============================================================ */
