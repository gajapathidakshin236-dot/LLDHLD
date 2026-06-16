package com.company.DSA;

import java.util.*;

/**
 * LeetCode #402 - Remove K Digits
 * Monotonic increasing stack; pop while top > current and k>0. Then trim leading zeros.
 * Time: O(n)  Space: O(n)
 */
public class RemoveKDigits {
    public String removeKdigits(String num, int k) {
        Deque<Character> st = new ArrayDeque<>();
        for (char c : num.toCharArray()) {
            while (!st.isEmpty() && k > 0 && st.peek() > c) { st.pop(); k--; }
            st.push(c);
        }
        while (k-- > 0 && !st.isEmpty()) st.pop();
        StringBuilder sb = new StringBuilder();
        while (!st.isEmpty()) sb.append(st.pollLast());
        int i = 0;
        while (i < sb.length() - 1 && sb.charAt(i) == '0') i++;
        String res = sb.substring(i);
        return res.isEmpty() ? "0" : res;
    }
}
