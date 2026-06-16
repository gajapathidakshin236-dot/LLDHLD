package com.company.DSA;

/**
 * LeetCode #137 - Single Number II
 * Every num appears 3x except one. Track ones/twos with bit logic.
 * Time: O(n)  Space: O(1)
 */
public class SingleNumberII {
    public int singleNumber(int[] nums) {
        int ones = 0, twos = 0;
        for (int x : nums) {
            ones = (ones ^ x) & ~twos;
            twos = (twos ^ x) & ~ones;
        }
        return ones;
    }
}
