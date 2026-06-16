package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #856 — Score of Parentheses
 * ============================================================
 *  PROBLEM
 *  -------
 *  Score of a balanced string s:
 *    "()" → 1
 *    AB   → A + B
 *    (A)  → 2 * A
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "()"     → 1
 *  Ex2: "(())"   → 2
 *  Ex3: "()()"   → 2
 *  Ex4: "(()(()))" → 6
 *
 *  CONSTRAINTS:  2 <= s.length <= 50; balanced.
 *
 *  HINTS
 *  -----
 *   1. Stack of partial scores. Push 0 on '('.
 *   2. On ')': pop v; result is max(2*v, 1); add to previous top.
 *   3. Single 0 left at start represents the running outer total.
 * ============================================================ */
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

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Stack of partial scores. Each '(' opens a new frame with score 0.
 *  Each ')' closes a frame:
 *    If inner score == 0 → it was a leaf "()" → contributes 1.
 *    Else → it had children → contributes 2 * inner.
 *  Add the contribution to the surrounding frame.
 *
 *  Why initial push(0):
 *    The outermost frame accumulates the overall result.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: nested expression evaluation with stack.
 * ============================================================ */
