package com.company.DSA;

/**
 * LeetCode #303 - Range Sum Query - Immutable
 * Prefix sums. sumRange(l, r) = pref[r+1] - pref[l].
 * Time: build O(n), query O(1)  Space: O(n)
 */
public class RangeSumImmutable {
    private final int[] pref;
    public RangeSumImmutable(int[] nums) {
        pref = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) pref[i + 1] = pref[i] + nums[i];
    }
    public int sumRange(int l, int r) { return pref[r + 1] - pref[l]; }
}
