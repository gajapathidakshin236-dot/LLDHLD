package com.company.DSA;

import java.util.*;

/**
 * LeetCode #856 - Score of Parentheses
 * Stack-based: push 0 on '('; on ')' pop v, push max(2v, 1) added to previous.
 * Time: O(n)  Space: O(n)
 */
public class ScoreOfParentheses {
    public int scoreOfParentheses(String s) {
        Deque<Integer> st = new ArrayDeque<>();
        st.push(0);
        for (char c : s.toCharArray()) {
            if (c == '(') st.push(0);
            else {
                int v = st.pop();
                int add = Math.max(2 * v, 1);
                st.push(st.pop() + add);
            }
        }
        return st.pop();
    }
}
