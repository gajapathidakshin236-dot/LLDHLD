package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #560 — Subarray Sum Equals K
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the number of contiguous subarrays whose sum equals exactly k.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[1,1,1], k=2 → 2  ([1,1] at [0..1] and [1..2])
 *  Ex2: nums=[1,2,3], k=3 → 2  ([1,2] and [3])
 *  Ex3: nums=[1],     k=0 → 0
 *  Ex4: nums=[-1,-1,1], k=0 → 1   (negatives allowed)
 *
 *  CONSTRAINTS:  1 <= n <= 2*10^4;  -1000 <= nums[i] <= 1000;  -10^7 <= k <= 10^7.
 *
 *  HINTS
 *  -----
 *   1. Brute force is O(n^2). Negatives prevent sliding-window approach.
 *   2. Use prefix sums: subarray sum (l..r) = prefix[r] - prefix[l-1].
 *   3. Count, for each r, how many earlier prefix values equal prefix[r] - k.
 * ============================================================ */
public class SubarraySumEqualsK {
    public int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        freq.put(0, 1);
        int sum = 0, count = 0;
        for (int x : nums) {
            sum += x;
            count += freq.getOrDefault(sum - k, 0);
            freq.merge(sum, 1, Integer::sum);
        }
        return count;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Prefix sums + HashMap of frequencies.
 *
 *  Key identity:
 *    sum(l..r) = prefix[r] - prefix[l-1]
 *    We want sum(l..r) == k ⇒ prefix[l-1] == prefix[r] - k.
 *    So at each r we ask: how many earlier prefixes equal prefix[r] - k?
 *
 *  Seed freq.put(0,1):
 *    Represents an empty prefix BEFORE index 0. Catches subarrays starting at 0.
 *
 *  Why sliding window WON'T work:
 *    Negatives can shrink and re-expand the sum unpredictably → monotonicity
 *    needed for sliding window is gone.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Edge cases: all zeros with k=0 → many; k>positive total → 0.
 *  Pattern: "prefix sum + hashmap of complements" — same trick: #325, #525, #974.
 * ============================================================ */
