package com.company.DSA;

import java.util.*;

/**
 * LeetCode #767 - Reorganize String
 * Greedy with max-heap of (count, char). Always place the highest count that isn't the previous.
 * Time: O(n log A)  Space: O(A)
 */
public class ReorganizeString {
    public String reorganizeString(String s) {
        int[] cnt = new int[26];
        for (char c : s.toCharArray()) cnt[c - 'a']++;
        int max = 0, mc = 0;
        for (int i = 0; i < 26; i++) if (cnt[i] > max) { max = cnt[i]; mc = i; }
        if (max > (s.length() + 1) / 2) return "";
        char[] res = new char[s.length()];
        int i = 0;
        while (cnt[mc]-- > 0) { res[i] = (char) ('a' + mc); i += 2; }
        for (int j = 0; j < 26; j++) {
            while (cnt[j]-- > 0) {
                if (i >= s.length()) i = 1;
                res[i] = (char) ('a' + j);
                i += 2;
            }
        }
        return new String(res);
    }
}
