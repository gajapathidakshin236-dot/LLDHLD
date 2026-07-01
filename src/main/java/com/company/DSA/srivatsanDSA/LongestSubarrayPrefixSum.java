package com.company.DSA.srivatsanDSA;

import java.util.HashMap;
import java.util.Map;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 48
 *  Problem: Longest Subarray with sum K (prefix-sum classic)
 *  e.g., nums = [1,-1,5,-2,3], k = 3 → length 4 ([1,-1,5,-2])
 *
 *  APPROACH (from notes):
 *    Prefix sum + HashMap of FIRST index of each prefix sum.
 *    If runningSum == k, longest so far is i + 1.
 *    Else, look up (runningSum - k) in the map; if present, longest candidate = i - firstIndex.
 *    Only store FIRST occurrence so the gap is maximised.
 * ============================================================ */
public class LongestSubarrayPrefixSum {

    public int longestSubarrayWithSumK(final int[] numbers, final int target) {
        final Map<Long, Integer> firstIndexByPrefix = new HashMap<>();
        long runningPrefixSum = 0L;
        int  longestLength    = 0;

        for (int index = 0; index < numbers.length; index++) {
            runningPrefixSum += numbers[index];

            if (runningPrefixSum == target) {
                longestLength = index + 1;
            }
            final long complementPrefix = runningPrefixSum - target;
            if (firstIndexByPrefix.containsKey(complementPrefix)) {
                longestLength = Math.max(longestLength, index - firstIndexByPrefix.get(complementPrefix));
            }
            firstIndexByPrefix.putIfAbsent(runningPrefixSum, index);
        }
        return longestLength;
    }
}
