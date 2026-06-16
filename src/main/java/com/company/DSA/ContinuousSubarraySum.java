package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #523 — Continuous Subarray Sum
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return true if nums has a CONTIGUOUS subarray of length >= 2 whose sum is
 *  a MULTIPLE of k.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[23,2,4,6,7], k=6 → true ([2,4])
 *  Ex2: nums=[23,2,6,4,7], k=6 → true ([23,2,6,4,7]=42 = 7*6)
 *  Ex3: nums=[23,2,6,4,7], k=13 → false
 *  Ex4: nums=[1,0],         k=2 → false  (sum 1 not multiple of 2)
 *  Ex5: nums=[0,0],         k=1 → true   (length-2 zero subarray)
 *
 *  CONSTRAINTS:  1 <= n <= 10^5;  0 <= val <= 10^9;  1 <= k <= 2^31 - 1.
 *
 *  HINTS
 *  -----
 *   1. (prefix[r] - prefix[l]) % k == 0 ⇔ prefix[r] % k == prefix[l] % k.
 *   2. Map "remainder" → earliest index seen.
 *   3. If the same remainder seen again with gap >= 2 → answer yes.
 *   4. Initialize remainder 0 at index -1 to handle subarrays starting at 0.
 * ============================================================ */
public class ContinuousSubarraySum {
    public boolean checkSubarraySum(int[] nums, int k) {
        Map<Integer,Integer> first = new HashMap<>();
        first.put(0, -1);
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            int r = sum % k;
            if (r < 0) r += k;
            if (first.containsKey(r)) { if (i - first.get(r) >= 2) return true; }
            else first.put(r, i);
        }
        return false;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two prefixes with the same remainder modulo k → their difference is a
 *  multiple of k. Track the FIRST index where each remainder is seen; if it
 *  comes up again at least 2 indices later, return true.
 *
 *  Why store FIRST (not LAST):
 *    Maximizes the gap, so if length-2 condition can be met, it's seen first.
 *
 *  Seed first.put(0, -1):
 *    Represents the empty prefix BEFORE index 0; lets subarrays starting at 0
 *    register.
 *
 *  Complexity: Time O(n), Space O(min(n, k)).
 *  Edge cases: zeros + length-2 → check k=1 case; negatives wrapped via `+k`.
 *  Pattern: prefix remainder hashing. Same: #974, #1590.
 * ============================================================ */
