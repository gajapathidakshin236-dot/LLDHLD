package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #17 — Letter Combinations of a Phone Number
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given a string of digits 2-9, return all possible letter combinations
 *  that the number could represent (standard phone keypad).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "23" → ["ad","ae","af","bd","be","bf","cd","ce","cf"]
 *  Ex2: ""   → []
 *  Ex3: "2"  → ["a","b","c"]
 *  Ex4: "7"  → ["p","q","r","s"]
 *
 *  CONSTRAINTS:  0 <= digits.length <= 4; digits in 2..9.
 *
 *  HINTS
 *  -----
 *   1. Map each digit to its letters.
 *   2. DFS picking one letter per digit position.
 * ============================================================ */
public class LetterCombinationsPhone {
    private static final String[] M = {"","","abc","def","ghi","jkl","mno","pqrs","tuv","wxyz"};
    public List<String> letterCombinations(String digits) {
        List<String> res = new ArrayList<>();
        if (digits.isEmpty()) return res;
        bt(digits, 0, new StringBuilder(), res);
        return res;
    }
    private void bt(String d, int i, StringBuilder cur, List<String> res) {
        if (i == d.length()) { res.add(cur.toString()); return; }
        for (char c : M[d.charAt(i) - '0'].toCharArray()) {
            cur.append(c);
            bt(d, i + 1, cur, res);
            cur.deleteCharAt(cur.length() - 1);
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Cartesian-product backtracking: 3-4 choices per digit. Append one letter,
 *  recurse to next digit, undo on the way back.
 *
 *  Complexity: Time O(3^n * 4^m) where n,m are counts of 3-letter/4-letter
 *  digits respectively. Space O(n) recursion.
 *  Pattern: small-branching backtracking — common interview warmup.
 * ============================================================ */
