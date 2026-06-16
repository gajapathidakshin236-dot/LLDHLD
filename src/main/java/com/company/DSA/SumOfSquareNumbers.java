package com.company.DSA;

/**
 * LeetCode #633 - Sum of Square Numbers
 * Two pointers a=0, b=sqrt(c); shrink/grow based on a^2+b^2 vs c.
 * Time: O(sqrt c)  Space: O(1)
 */
public class SumOfSquareNumbers {
    public boolean judgeSquareSum(int c) {
        long a = 0, b = (long) Math.sqrt(c);
        while (a <= b) {
            long s = a * a + b * b;
            if (s == c) return true;
            if (s < c) a++; else b--;
        }
        return false;
    }
}
