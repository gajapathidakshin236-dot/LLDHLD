package com.company.DSA.srivatsanDSA;

import java.util.ArrayList;
import java.util.List;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 46
 *  Problem: Generate Parentheses — all valid strings of n pairs.
 *
 *  APPROACH (from notes):
 *    Backtrack tracking open & close counts.
 *      if length == 2*n → add and return.
 *      if open  < n     → append '(' and recurse with open + 1.
 *      if close < open  → append ')' and recurse with close + 1.
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
