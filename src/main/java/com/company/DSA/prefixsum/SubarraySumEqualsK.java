package com.company.DSA.prefixsum;

import java.util.*;

/* ============================================================
 *  LeetCode #560 — Subarray Sum Equals K
 * ============================================================
 *  PROBLEM
 *  -------
 *  Count contiguous subarrays whose sum equals exactly k.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[1,1,1], k=2 → 2
 *  Ex2: nums=[1,2,3], k=3 → 2
 *  Ex3: nums=[-1,-1,1], k=0 → 1
 *
 *  CONSTRAINTS:  1 <= n <= 2*10^4;  -1000 <= val <= 1000.
 *
 *  HINTS
 *  -----
 *   1. prefix[r] - prefix[l-1] == k ⇔ prefix[l-1] == prefix[r] - k.
 *   2. HashMap of prefix-sum frequencies.
 * ============================================================ */
public class SubarraySumEqualsK {

    public int subarraySum(final int[] numbers, final int targetSum) {
        final Map<Integer, Integer> prefixSumFrequency = new HashMap<>();
        prefixSumFrequency.put(0, 1);

        int runningPrefixSum = 0;
        int matchingSubarrayCount = 0;

        for (final int value : numbers) {
            runningPrefixSum += value;
            matchingSubarrayCount += prefixSumFrequency.getOrDefault(runningPrefixSum - targetSum, 0);
            prefixSumFrequency.merge(runningPrefixSum, 1, Integer::sum);
        }
        return matchingSubarrayCount;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Prefix sum + HashMap of complement counts. Seed (0, 1) for subarrays starting at 0.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: prefix sum + hashmap of complements.
 * ============================================================ */
