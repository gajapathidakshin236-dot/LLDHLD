package com.company.DSA.stack;

import java.util.*;

/* ============================================================
 *  LeetCode #20 — Valid Parentheses
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return true if all brackets are properly opened and closed in matching order.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "()"        → true
 *  Ex2: "()[]{}"    → true
 *  Ex3: "(]"        → false
 *  Ex4: "([)]"      → false
 *  Ex5: "{[]}"      → true
 *
 *  CONSTRAINTS:  1 <= s.length <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. LIFO behavior → stack.
 *   2. On open, push the EXPECTED closing bracket.
 * ============================================================ */
public class ValidParentheses {

    public boolean isValid(final String brackets) {
        final Deque<Character> expectedClosers = new ArrayDeque<>();

        for (final char character : brackets.toCharArray()) {
            switch (character) {
                case '(': expectedClosers.push(')'); break;
                case '[': expectedClosers.push(']'); break;
                case '{': expectedClosers.push('}'); break;
                default:
                    if (expectedClosers.isEmpty() || expectedClosers.pop() != character) {
                        return false;
                    }
            }
        }
        return expectedClosers.isEmpty();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Stack of expected closers. Each open pushes its matching close; each close
 *  must match the top of the stack.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: stack matching.
 * ============================================================ */
