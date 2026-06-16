package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #974 — Subarray Sums Divisible by K
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given an array nums and integer k, return the number of non-empty
 *  subarrays whose sum is divisible by k.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[4,5,0,-2,-3,1], k=5 → 7
 *  Ex2: nums=[5],             k=9 → 0
 *  Ex3: nums=[-1,2,9],        k=2 → 2
 *
 *  CONSTRAINTS:  1 <= n <= 3*10^4;  -10^4 <= val <= 10^4;  2 <= k <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Subarray sum (l..r) ≡ 0 (mod k) ⇔ prefix[r] ≡ prefix[l-1] (mod k).
 *   2. Count prefix sums BY REMAINDER.
 *   3. Each pair of indices sharing a remainder = one valid subarray.
 *   4. Normalize remainders to [0, k) — handle negative mods.
 * ============================================================ */
public class SubarraySumsDivisibleByK {
    public int subarraysDivByK(int[] nums, int k) {
        int[] cnt = new int[k];
        cnt[0] = 1;
        int sum = 0, ans = 0;
        for (int x : nums) {
            sum += x;
            int r = ((sum % k) + k) % k;
            ans += cnt[r]++;
        }
        return ans;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Prefix sums modulo k. For each prefix remainder r, pair each new occurrence
 *  with every previous one at the same remainder → ans += cnt[r], then ++cnt[r].
 *
 *  Why ((sum % k) + k) % k:
 *    In Java, % can return negative for negative sum (e.g., -3 % 5 == -3).
 *    We need a non-negative remainder index in [0, k).
 *
 *  Seed cnt[0] = 1:
 *    The empty prefix has sum 0 — needed to count subarrays starting at index 0.
 *
 *  Complexity: Time O(n), Space O(k).
 *  Edge cases: all zeros → many pairs; n=1 → answer depends on a[0] % k.
 *  Pattern: prefix sum + count by remainder. Cousin of #560, #523.
 * ============================================================ */
