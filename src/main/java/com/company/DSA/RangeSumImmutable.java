package com.company.DSA;

/* ============================================================
 *  LeetCode #303 — Range Sum Query - Immutable
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given immutable array nums, support sumRange(l, r) returning sum of nums[l..r]
 *  in O(1) per query.
 *
 *  EXAMPLES
 *  --------
 *  nums=[-2,0,3,-5,2,-1]
 *  sumRange(0,2) → 1   (-2+0+3)
 *  sumRange(2,5) → -1
 *  sumRange(0,5) → -3
 *
 *  CONSTRAINTS:  1 <= n <= 10^4; sumRange called <= 10^4 times.
 *
 *  HINTS
 *  -----
 *   1. Precompute prefix sums; sumRange(l,r) = pref[r+1] - pref[l].
 *   2. Allocate pref[n+1] with pref[0]=0 to avoid l==0 special case.
 * ============================================================ */
public class RangeSumImmutable {
    private final int[] pref;
    public RangeSumImmutable(int[] nums) {
        pref = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) pref[i + 1] = pref[i] + nums[i];
    }
    public int sumRange(int l, int r) { return pref[r + 1] - pref[l]; }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Classic prefix-sum precompute:
 *    pref[i] = nums[0] + nums[1] + ... + nums[i-1]
 *  Then sum of nums[l..r] = pref[r+1] - pref[l].
 *
 *  Why pref[n+1] with leading 0:
 *    Lets us avoid `l == 0 ? pref[r] : pref[r] - pref[l-1]` ugliness.
 *
 *  Complexity: Build O(n), query O(1), space O(n).
 *  Edge cases: l == r → returns single element; l == 0 covered by leading 0.
 *  Pattern: precompute trade-off — extra space for instant query. Same family:
 *           2D #304, mutable #307 (segment tree / BIT).
 * ============================================================ */
