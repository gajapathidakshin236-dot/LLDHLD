package com.company.DSA;

/**
 * LeetCode #2390 - Removing Stars From a String
 * StringBuilder as stack: on '*' remove last char, else append.
 * Time: O(n)  Space: O(n)
 */
public class RemovingStarsFromString {
    public String removeStars(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c == '*') sb.deleteCharAt(sb.length() - 1);
            else sb.append(c);
        }
        return sb.toString();
    }
}
