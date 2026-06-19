package com.company.DSA.bitmanip;

/* ============================================================
 *  LeetCode #137 — Single Number II
 * ============================================================
 *  PROBLEM
 *  -------
 *  One element appears once; all others 3 times. Find it. O(n)/O(1).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [2,2,3,2] → 3
 *  Ex2: [0,1,0,1,0,1,99] → 99
 *
 *  CONSTRAINTS:  1 <= n <= 3*10^4.
 *
 *  HINTS
 *  -----
 *   1. Track per-bit count modulo 3 via two ints (ones, twos).
 * ============================================================ */
public class SingleNumberII {

    public int singleNumber(final int[] numbers) {
        int bitsSeenOnce  = 0;
        int bitsSeenTwice = 0;

        for (final int value : numbers) {
            bitsSeenOnce  = (bitsSeenOnce  ^ value) & ~bitsSeenTwice;
            bitsSeenTwice = (bitsSeenTwice ^ value) & ~bitsSeenOnce;
        }
        return bitsSeenOnce;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Bit-level mod-3 counter implemented with two ints.
 *  Final `bitsSeenOnce` holds the unique number's bits.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: digital simulation with bit ops.
 * ============================================================ */
