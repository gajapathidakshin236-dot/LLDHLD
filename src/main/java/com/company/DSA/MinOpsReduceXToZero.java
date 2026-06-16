package com.company.DSA;

/* ============================================================
 *  LeetCode #1658 — Minimum Operations to Reduce X to Zero
 * ============================================================
 *  PROBLEM
 *  -------
 *  You can remove one element from either end of nums at a time, subtracting
 *  its value from x. Return the minimum number of removals to make x exactly
 *  zero, or -1.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: nums=[1,1,4,2,3], x=5 → 2  (remove 3 and 2)
 *  Ex2: nums=[5,6,7,8,9], x=4 → -1
 *  Ex3: nums=[3,2,20,1,1,3], x=10 → 5
 *
 *  CONSTRAINTS:  1 <= n <= 10^5;  1 <= val <= 10^4;  1 <= x <= 10^9.
 *
 *  HINTS
 *  -----
 *   1. Transform: remove a PREFIX of length p and SUFFIX of length s with sum = x.
 *      Equivalent: find LONGEST middle subarray with sum = total - x.
 *   2. Sliding window because all values are positive.
 * ============================================================ */
public class MinOpsReduceXToZero {
    public int minOperations(int[] nums, int x) {
        int total = 0;
        for (int v : nums) total += v;
        int need = total - x;
        if (need < 0) return -1;
        int l = 0, sum = 0, best = -1;
        for (int r = 0; r < nums.length; r++) {
            sum += nums[r];
            while (sum > need) sum -= nums[l++];
            if (sum == need) best = Math.max(best, r - l + 1);
        }
        return best < 0 ? -1 : nums.length - best;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Reframe: instead of finding what to REMOVE, find the LONGEST middle subarray
 *  we KEEP. Its sum must equal totalSum - x. Sliding window since values are positive.
 *  Answer: n - longestKept (or -1 if no such window).
 *
 *  Edge case `need == 0`:
 *    Means we must remove ALL → best is 0 (the empty subarray) → ans = n.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: "complement reformulation." Same idea: longest subarray with
 *           given sum (#560 variant), min window with given sum.
 * ============================================================ */
