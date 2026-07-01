package com.company.DSA.srivatsanDSA;

import java.util.Arrays;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 74
 *  Problem: Longest Increasing Subsequence
 *
 *  APPROACH (from notes):
 *    DP O(n^2):
 *      dp[i] = length of LIS ending at i.
 *      dp[i] = 1 + max(dp[j]) for all j < i where nums[j] < nums[i].
 *      Answer = max(dp).
 *    Notes also mention the O(n log n) patience-sort / binary-search method
 *    (maintain "tails" array, replace via binary search).
 * ============================================================ */
public class LongestIncreasingSubsequence {

    public int lengthOfLIS(final int[] numbers) {
        final int[] lengthEndingAtIndex = new int[numbers.length];
        Arrays.fill(lengthEndingAtIndex, 1);

        int bestLength = 1;
        for (int currentIndex = 1; currentIndex < numbers.length; currentIndex++) {
            for (int previousIndex = 0; previousIndex < currentIndex; previousIndex++) {
                if (numbers[previousIndex] < numbers[currentIndex]) {
                    lengthEndingAtIndex[currentIndex] = Math.max(
                            lengthEndingAtIndex[currentIndex],
                            lengthEndingAtIndex[previousIndex] + 1);
                }
            }
            bestLength = Math.max(bestLength, lengthEndingAtIndex[currentIndex]);
        }
        return bestLength;
    }
}
