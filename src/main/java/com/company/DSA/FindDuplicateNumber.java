package com.company.DSA;

/**
 * LeetCode #287 - Find the Duplicate Number
 * Floyd's cycle on f(i) = nums[i]. Phase 1: meet. Phase 2: head & meet 1-step to cycle entrance.
 * Time: O(n)  Space: O(1)
 */
public class FindDuplicateNumber {
    public int findDuplicate(int[] nums) {
        int s = nums[0], f = nums[0];
        do { s = nums[s]; f = nums[nums[f]]; } while (s != f);
        int p = nums[0];
        while (p != s) { p = nums[p]; s = nums[s]; }
        return p;
    }
}
