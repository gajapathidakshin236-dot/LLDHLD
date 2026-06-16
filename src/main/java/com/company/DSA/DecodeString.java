package com.company.DSA;

import java.util.*;

/**
 * LeetCode #394 - Decode String
 * Two stacks: counts and built strings. On '[' push current, reset; on ']' pop and repeat.
 * Time: O(n * maxK)  Space: O(n)
 */
public class DecodeString {
    public String decodeString(String s) {
        Deque<Integer> counts = new ArrayDeque<>();
        Deque<StringBuilder> strs = new ArrayDeque<>();
        StringBuilder cur = new StringBuilder();
        int k = 0;
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) k = k * 10 + (c - '0');
            else if (c == '[') { counts.push(k); strs.push(cur); k = 0; cur = new StringBuilder(); }
            else if (c == ']') {
                int rep = counts.pop();
                StringBuilder prev = strs.pop();
                for (int i = 0; i < rep; i++) prev.append(cur);
                cur = prev;
            } else cur.append(c);
        }
        return cur.toString();
    }
}
