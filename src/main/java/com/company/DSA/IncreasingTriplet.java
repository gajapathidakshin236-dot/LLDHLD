package com.company.DSA;

/**
 * LeetCode #334 - Increasing Triplet Subsequence
 * Maintain smallest (a) and second smallest (b); if any x>b found, triplet exists.
 * Time: O(n)  Space: O(1)
 */
public class IncreasingTriplet {
    public boolean increasingTriplet(int[] nums) {
        int a = Integer.MAX_VALUE, b = Integer.MAX_VALUE;
        for (int x : nums) {
            if (x <= a) a = x;
            else if (x <= b) b = x;
            else return true;
        }
        return false;
    }
}
