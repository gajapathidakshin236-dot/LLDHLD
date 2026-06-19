package com.company.DSA.slidingwindow;

/* ============================================================
 *  LeetCode #1652 — Defuse the Bomb
 * ============================================================
 *  PROBLEM
 *  -------
 *  Replace each code[i] with sum of next k (if k>0) or previous |k| (if k<0)
 *  in a circular array. k=0 → zero out.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: code=[5,7,1,4], k=3   → [12,10,16,13]
 *  Ex2: code=[1,2,3,4], k=0   → [0,0,0,0]
 *  Ex3: code=[2,4,9,3], k=-2  → [12,5,6,13]
 *
 *  CONSTRAINTS:  1 <= n <= 100; -(n-1) <= k <= n-1.
 *
 *  HINTS
 *  -----
 *   1. Modulo with `+n` to wrap negatives.
 * ============================================================ */
public class DefuseTheBomb {

    public int[] decrypt(final int[] code, final int neighborOffset) {
        final int length = code.length;
        final int[] result = new int[length];

        if (neighborOffset == 0) {
            return result;
        }

        for (int targetIndex = 0; targetIndex < length; targetIndex++) {
            result[targetIndex] = sumNeighbors(code, targetIndex, neighborOffset);
        }
        return result;
    }

    private int sumNeighbors(final int[] code, final int targetIndex, final int neighborOffset) {
        final int length = code.length;
        int sum = 0;

        if (neighborOffset > 0) {
            for (int step = 1; step <= neighborOffset; step++) {
                sum += code[(targetIndex + step) % length];
            }
        } else {
            for (int step = 1; step <= -neighborOffset; step++) {
                sum += code[((targetIndex - step) % length + length) % length];
            }
        }
        return sum;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Direct simulation with circular indexing. Modular arithmetic handles wrap.
 *
 *  Complexity: Time O(n * |k|), Space O(n).
 *  Pattern: circular array simulation.
 * ============================================================ */
