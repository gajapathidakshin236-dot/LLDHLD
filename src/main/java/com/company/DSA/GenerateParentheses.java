package com.company.DSA;

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
 *  Ex4: n=0 → [""]
 *
 *  CONSTRAINTS:  1 <= n <= 8.
 *
 *  HINTS
 *  -----
 *   1. Backtrack. At each step decide '(' or ')'.
 *   2. Add '(' only if open count < n.
 *   3. Add ')' only if close count < open count (keeps it valid as we build).
 * ============================================================ */
public class GenerateParentheses {
    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        bt(res, new StringBuilder(), 0, 0, n);
        return res;
    }
    private void bt(List<String> res, StringBuilder cur, int open, int close, int n) {
        if (cur.length() == 2 * n) { res.add(cur.toString()); return; }
        if (open < n)    { cur.append('('); bt(res, cur, open + 1, close, n); cur.deleteCharAt(cur.length()-1); }
        if (close < open){ cur.append(')'); bt(res, cur, open, close + 1, n); cur.deleteCharAt(cur.length()-1); }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Backtracking with two pruning conditions to ensure validity AS WE BUILD:
 *    1) Can place '(' only if open < n.
 *    2) Can place ')' only if close < open (else string becomes invalid).
 *  When length == 2n, append to result.
 *
 *  Why this beats "generate all and filter":
 *    Filtering wastes work on invalid strings. Pruning avoids invalid subtrees.
 *
 *  Complexity: Time O(C_n) — n-th Catalan number. Space O(n) for recursion + StringBuilder.
 *  Edge cases: n=0 → [""] (vacuous valid empty).
 *  Pattern: validity-preserving backtracking. Common in well-formedness problems.
 * ============================================================ */
