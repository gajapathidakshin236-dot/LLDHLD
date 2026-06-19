package com.company.DSA.twopointers;

/* ============================================================
 *  LeetCode #633 — Sum of Square Numbers
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return true if a^2 + b^2 = c for some non-negative ints a, b.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: c=5 → true (1^2 + 2^2)
 *  Ex2: c=3 → false
 *  Ex3: c=0 → true (0+0)
 *
 *  CONSTRAINTS:  0 <= c <= 2^31 - 1.
 *
 *  HINTS
 *  -----
 *   1. a = 0, b = sqrt(c). Move a up if too small, b down if too big.
 *   2. Use long.
 * ============================================================ */
public class SumOfSquareNumbers {

    public boolean judgeSquareSum(final int targetValue) {
        long smallSquareRoot = 0;
        long largeSquareRoot = (long) Math.sqrt(targetValue);

        while (smallSquareRoot <= largeSquareRoot) {
            final long squareSum = smallSquareRoot * smallSquareRoot
                                 + largeSquareRoot * largeSquareRoot;

            if (squareSum == targetValue) {
                return true;
            }
            if (squareSum < targetValue) {
                smallSquareRoot++;
            } else {
                largeSquareRoot--;
            }
        }
        return false;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two pointers a=0, b=sqrt(c). Monotonic movement converges in O(sqrt c).
 *
 *  Complexity: Time O(sqrt c), Space O(1).
 *  Pattern: two-pointer over a smooth function.
 * ============================================================ */
