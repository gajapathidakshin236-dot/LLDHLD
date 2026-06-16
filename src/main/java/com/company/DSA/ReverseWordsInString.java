package com.company.DSA;

/**
 * LeetCode #151 - Reverse Words in a String
 * trim, split on whitespace (regex \\s+), reverse list, join with single space.
 * Time: O(n)  Space: O(n)
 */
public class ReverseWordsInString {
    public String reverseWords(String s) {
        String[] parts = s.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = parts.length - 1; i >= 0; i--) {
            sb.append(parts[i]);
            if (i > 0) sb.append(' ');
        }
        return sb.toString();
    }
}
