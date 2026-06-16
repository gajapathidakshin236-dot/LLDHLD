package com.company.DSA;

/**
 * LeetCode #791 - Custom Sort String
 * Count chars in s; emit chars in order by counts; append remaining chars not in order.
 * Time: O(n+m)  Space: O(1)
 */
public class CustomSortString {
    public String customSortString(String order, String s) {
        int[] cnt = new int[26];
        for (char c : s.toCharArray()) cnt[c - 'a']++;
        StringBuilder sb = new StringBuilder();
        for (char c : order.toCharArray()) {
            while (cnt[c - 'a']-- > 0) sb.append(c);
        }
        for (int i = 0; i < 26; i++)
            while (cnt[i]-- > 0) sb.append((char) ('a' + i));
        return sb.toString();
    }
}
