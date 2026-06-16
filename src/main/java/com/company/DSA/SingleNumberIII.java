package com.company.DSA;

/**
 * LeetCode #260 - Single Number III
 * XOR all -> a^b. Take lowest set bit; split nums into two groups; XOR each.
 * Time: O(n)  Space: O(1)
 */
public class SingleNumberIII {
    public int[] singleNumber(int[] nums) {
        int xor = 0;
        for (int x : nums) xor ^= x;
        int bit = xor & -xor;
        int a = 0, b = 0;
        for (int x : nums) {
            if ((x & bit) == 0) a ^= x; else b ^= x;
        }
        return new int[]{a, b};
    }
}
