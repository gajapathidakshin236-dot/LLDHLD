package com.company.DSA.bitmanip;

/* ============================================================
 *  LeetCode #260 — Single Number III
 * ============================================================
 *  PROBLEM
 *  -------
 *  Two elements appear once; all others twice. Find both. O(n)/O(1).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,1,3,2,5] → [3,5]
 *  Ex2: [-1,0]        → [-1,0]
 *
 *  CONSTRAINTS:  2 <= n <= 3*10^4.
 *
 *  HINTS
 *  -----
 *   1. XOR all → a^b. Pick its lowest set bit; partition; XOR each half.
 * ============================================================ */
public class SingleNumberIII {

    public int[] singleNumber(final int[] numbers) {
        int xorOfAll = 0;
        for (final int value : numbers) {
            xorOfAll ^= value;
        }

        final int lowestSetBit = xorOfAll & -xorOfAll;
        int xorOfGroupA = 0;
        int xorOfGroupB = 0;

        for (final int value : numbers) {
            if ((value & lowestSetBit) == 0) {
                xorOfGroupA ^= value;
            } else {
                xorOfGroupB ^= value;
            }
        }
        return new int[] { xorOfGroupA, xorOfGroupB };
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  XOR all gives a^b. Picking the lowest set bit splits the array into two
 *  groups, each containing exactly one of the singletons.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: XOR partitioning.
 * ============================================================ */
