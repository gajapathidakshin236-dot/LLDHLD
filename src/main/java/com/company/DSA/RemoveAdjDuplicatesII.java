package com.company.DSA;

import java.util.*;

/**
 * LeetCode #1209 - Remove All Adjacent Duplicates in String II
 * Stack of (char, count). On match increment; if reaches k, pop.
 * Time: O(n)  Space: O(n)
 */
public class RemoveAdjDuplicatesII {
    public String removeDuplicates(String s, int k) {
        Deque<int[]> st = new ArrayDeque<>(); // [charAsInt, count]
        for (char c : s.toCharArray()) {
            if (!st.isEmpty() && st.peek()[0] == c) {
                if (++st.peek()[1] == k) st.pop();
            } else st.push(new int[]{c, 1});
        }
        StringBuilder sb = new StringBuilder();
        for (int[] e : st) {
            char c = (char) e[0];
            for (int i = 0; i < e[1]; i++) sb.append(c);
        }
        return sb.reverse().toString();
    }
}
