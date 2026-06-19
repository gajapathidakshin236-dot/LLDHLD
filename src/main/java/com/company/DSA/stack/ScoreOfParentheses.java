package com.company.DSA.stack;

import java.util.*;

/* ============================================================
 *  LeetCode #856 — Score of Parentheses
 * ============================================================
 *  PROBLEM
 *  -------
 *  Score: "()" = 1; AB = A+B; (A) = 2*A.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "()"       → 1
 *  Ex2: "(())"     → 2
 *  Ex3: "()()"     → 2
 *  Ex4: "(()(()))" → 6
 *
 *  CONSTRAINTS:  2 <= s.length <= 50; balanced.
 *
 *  HINTS
 *  -----
 *   1. Stack of partial scores; push 0 on '('; on ')' pop and apply max(2*v, 1).
 * ============================================================ */
public class ScoreOfParentheses {

    public int scoreOfParentheses(final String brackets) {
        final Deque<Integer> partialScoreStack = new ArrayDeque<>();
        partialScoreStack.push(0);

        for (final char character : brackets.toCharArray()) {
            if (character == '(') {
                partialScoreStack.push(0);
            } else {
                final int innerScore       = partialScoreStack.pop();
                final int closedFrameValue = Math.max(2 * innerScore, 1);
                partialScoreStack.push(partialScoreStack.pop() + closedFrameValue);
            }
        }
        return partialScoreStack.pop();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Stack of partial scores. Each '(' opens a new frame with 0. Each ')' closes:
 *    inner = 0 → leaf "()" = 1; else 2 * inner. Add to outer frame.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: nested expression evaluation with stack.
 * ============================================================ */
