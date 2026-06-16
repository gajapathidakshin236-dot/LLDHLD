package com.company.DSA;

/**
 * LeetCode #8 - String to Integer (atoi)
 * 1) skip leading spaces  2) optional sign  3) read digits  4) clamp to int range
 * Time: O(n)  Space: O(1)
 */
public class StringToInteger {
    public int myAtoi(String s) {
        int i = 0, n = s.length(), sign = 1;
        long res = 0;
        while (i < n && s.charAt(i) == ' ') i++;
        if (i < n && (s.charAt(i) == '+' || s.charAt(i) == '-')) {
            sign = s.charAt(i) == '-' ? -1 : 1;
            i++;
        }
        while (i < n && Character.isDigit(s.charAt(i))) {
            res = res * 10 + (s.charAt(i) - '0');
            if (sign * res >  Integer.MAX_VALUE) return Integer.MAX_VALUE;
            if (sign * res <  Integer.MIN_VALUE) return Integer.MIN_VALUE;
            i++;
        }
        return (int)(sign * res);
    }
}
