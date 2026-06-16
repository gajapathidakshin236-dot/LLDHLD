package com.company.DSA;

import java.util.*;

/**
 * LeetCode #946 - Validate Stack Sequences
 * Simulate with a stack; after each push pop while top==popped[j].
 * Time: O(n)  Space: O(n)
 */
public class ValidateStackSequences {
    public boolean validateStackSequences(int[] pushed, int[] popped) {
        Deque<Integer> st = new ArrayDeque<>();
        int j = 0;
        for (int v : pushed) {
            st.push(v);
            while (!st.isEmpty() && st.peek() == popped[j]) { st.pop(); j++; }
        }
        return st.isEmpty();
    }
}
