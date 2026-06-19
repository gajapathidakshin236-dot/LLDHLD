package com.company.DSA.prefixsum;

/* ============================================================
 *  LeetCode #303 — Range Sum Query - Immutable
 * ============================================================
 *  PROBLEM
 *  -------
 *  sumRange(l, r) = sum of nums[l..r] in O(1) per query.
 *
 *  EXAMPLES
 *  --------
 *  nums=[-2,0,3,-5,2,-1]
 *  sumRange(0,2) → 1
 *  sumRange(2,5) → -1
 *  sumRange(0,5) → -3
 *
 *  CONSTRAINTS:  1 <= n <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Prefix sum with leading 0 to avoid l==0 special case.
 * ============================================================ */
public class RangeSumImmutable {

    private final int[] prefixSum;

    public RangeSumImmutable(final int[] numbers) {
        this.prefixSum = new int[numbers.length + 1];
        for (int index = 0; index < numbers.length; index++) {
            prefixSum[index + 1] = prefixSum[index] + numbers[index];
        }
    }

    public int sumRange(final int leftIndex, final int rightIndex) {
        return prefixSum[rightIndex + 1] - prefixSum[leftIndex];
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  prefix[i] = sum nums[0..i-1]. sumRange(l, r) = prefix[r+1] - prefix[l].
 *
 *  Complexity: Build O(n), query O(1), space O(n).
 *  Pattern: precompute trade-off — extra space for instant query.
 * ============================================================ */
