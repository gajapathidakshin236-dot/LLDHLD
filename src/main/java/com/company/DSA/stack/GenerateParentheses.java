package com.company.DSA.stack;

import java.util.*;

/* ============================================================
 *  LeetCode #22 — Generate Parentheses
 * ============================================================
 *  PROBLEM
 *  -------
 *  Generate all distinct strings of n well-formed pairs of parentheses.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: n=3 → ["((()))","(()())","(())()","()(())","()()()"]
 *  Ex2: n=1 → ["()"]
 *  Ex3: n=2 → ["(())","()()"]
 *
 *  CONSTRAINTS:  1 <= n <= 8.
 *
 *  HINTS
 *  -----
 *   1. Backtrack with constraints: add '(' only if open < n, add ')' only if close < open.
 * ============================================================ */
public class GenerateParentheses {

    public List<String> generateParenthesis(final int pairsCount) {
        final List<String> results = new ArrayList<>();
        backtrack(results, new StringBuilder(), 0, 0, pairsCount);
        return results;
    }

    private void backtrack(final List<String> results,
                           final StringBuilder currentBuffer,
                           final int openCount,
                           final int closeCount,
                           final int pairsCount) {
        if (currentBuffer.length() == 2 * pairsCount) {
            results.add(currentBuffer.toString());
            return;
        }

        if (openCount < pairsCount) {
            currentBuffer.append('(');
            backtrack(results, currentBuffer, openCount + 1, closeCount, pairsCount);
            currentBuffer.deleteCharAt(currentBuffer.length() - 1);
        }
        if (closeCount < openCount) {
            currentBuffer.append(')');
            backtrack(results, currentBuffer, openCount, closeCount + 1, pairsCount);
            currentBuffer.deleteCharAt(currentBuffer.length() - 1);
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Backtracking with validity-preserving pruning. Add '(' only when room;
 *  add ')' only when it can close a pending '('.
 *
 *  Complexity: Time O(Catalan_n), Space O(n).
 *  Pattern: validity-preserving backtracking.
 * ============================================================ */
