package com.company.DSA;

/* ============================================================
 *  LeetCode #921 — Minimum Add to Make Parentheses Valid
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the minimum number of parentheses (open or close) you must add to
 *  make a string of '(' and ')' valid.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "())"   → 1   (add one '(' at front OR remove one ')')
 *  Ex2: "((("   → 3
 *  Ex3: "()"    → 0
 *  Ex4: "()))(("  → 4
 *
 *  CONSTRAINTS:  1 <= s.length <= 1000; only '(' and ')'.
 *
 *  HINTS
 *  -----
 *   1. Track "open" balance. On '(' increment.
 *   2. On ')' decrement IF balance > 0; else `need` counts unmatched closers.
 *   3. Answer = need + open.
 * ============================================================ */
public class MinAddToMakeParenValid {
    public int minAddToMakeValid(String s) {
        int open = 0, need = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') open++;
            else if (open > 0) open--;
            else need++;
        }
        return open + need;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Walk once. `open` = unmatched '(' so far, `need` = required '(' we couldn't
 *  find for ')'. Final answer is open + need: each unmatched paren needs its
 *  partner inserted.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Edge cases: balanced → 0; all opens → length; all closes → length.
 *  Pattern: balance counters. Same: #1249, #1614.
 * ============================================================ */
