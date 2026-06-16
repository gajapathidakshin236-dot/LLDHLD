package com.company.DSA;

/**
 * LeetCode #1567 - Maximum Length of Subarray With Positive Product
 * Track pos = longest ending here with +ve product, neg = with -ve product.
 * Time: O(n)  Space: O(1)
 */
public class MaxLenPositiveProduct {
    public int getMaxLen(int[] nums) {
        int pos = 0, neg = 0, best = 0;
        for (int x : nums) {
            if (x == 0) { pos = 0; neg = 0; }
            else if (x > 0) { pos++; neg = neg > 0 ? neg + 1 : 0; }
            else { int p = pos; pos = neg > 0 ? neg + 1 : 0; neg = p + 1; }
            best = Math.max(best, pos);
        }
        return best;
    }
}
