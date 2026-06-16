package com.company.DSA;

/**
 * LeetCode #880 - Decoded String at Index
 * Compute total decoded size first. Walk back, mod-reducing k by current length.
 * Time: O(n)  Space: O(1)
 */
public class DecodedStringAtIndex {
    public String decodeAtIndex(String s, int k) {
        long size = 0;
        for (char c : s.toCharArray())
            size = Character.isDigit(c) ? size * (c - '0') : size + 1;
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            k %= size;
            if (k == 0 && Character.isLetter(c)) return String.valueOf(c);
            if (Character.isDigit(c)) size /= (c - '0');
            else size--;
        }
        return "";
    }
}
