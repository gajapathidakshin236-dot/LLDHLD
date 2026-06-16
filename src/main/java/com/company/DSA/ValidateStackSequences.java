package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #946 — Validate Stack Sequences
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given two integer sequences pushed and popped (same length), return true
 *  if and only if they could be the result of a sequence of push/pop on an
 *  empty stack.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: pushed=[1,2,3,4,5], popped=[4,5,3,2,1] → true
 *  Ex2: pushed=[1,2,3,4,5], popped=[4,3,5,1,2] → false
 *  Ex3: pushed=[1,2], popped=[2,1] → true
 *
 *  CONSTRAINTS:  1 <= n <= 1000; values unique 0..1000.
 *
 *  HINTS
 *  -----
 *   1. Simulate. Push values one by one; after each push, pop while top equals popped[j].
 *   2. At the end, stack should be empty.
 * ============================================================ */
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

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Simulate the stack. Push every value in order; after each push, greedily
 *  pop the top whenever it matches popped[j]. If at the end the stack is empty,
 *  the sequence is valid; otherwise some required pop never matched the top.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: greedy stack simulation. Cousin of stock-span/monotonic-stack.
 * ============================================================ */
