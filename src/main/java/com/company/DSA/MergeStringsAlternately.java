package com.company.DSA;

/**
 * LeetCode #1768 - Merge Strings Alternately
 * Two indices alternate; append remainder of longer at end.
 * Time: O(n+m)  Space: O(n+m)
 */
public class MergeStringsAlternately {
    public String mergeAlternately(String a, String b) {
        StringBuilder sb = new StringBuilder();
        int i = 0, j = 0;
        while (i < a.length() && j < b.length()) { sb.append(a.charAt(i++)).append(b.charAt(j++)); }
        sb.append(a, i, a.length()).append(b, j, b.length());
        return sb.toString();
    }
}
