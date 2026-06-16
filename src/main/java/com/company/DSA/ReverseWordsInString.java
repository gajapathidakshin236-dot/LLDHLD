package com.company.DSA;

/* ============================================================
 *  LeetCode #151 — Reverse Words in a String
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given a string s, return the words reversed, separated by a single space.
 *  Leading/trailing spaces stripped; multiple internal spaces collapsed.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "the sky is blue"      → "blue is sky the"
 *  Ex2: "  hello world  "      → "world hello"
 *  Ex3: "a good   example"     → "example good a"
 *  Ex4: "  Bob    Loves  Alice " → "Alice Loves Bob"
 *
 *  CONSTRAINTS:  1 <= s.length <= 10^4; ASCII letters/digits/spaces.
 *
 *  HINTS
 *  -----
 *   1. Java: s.trim() then split("\\s+") handles multi-space + edges.
 *   2. Walk the array backwards, joining with single space.
 *   3. Two-pointer in-place O(1)-extra version exists for the follow-up.
 * ============================================================ */
public class ReverseWordsInString {
    public String reverseWords(String s) {
        String[] parts = s.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = parts.length - 1; i >= 0; i--) {
            sb.append(parts[i]);
            if (i > 0) sb.append(' ');
        }
        return sb.toString();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  trim() removes leading/trailing whitespace; split("\\s+") collapses any
 *  run of whitespace into a single split, yielding clean word tokens.
 *  Walk the tokens backwards into a StringBuilder.
 *
 *  Follow-up — O(1) extra in C-style:
 *    1) Reverse the whole string.
 *    2) Reverse each word in place.
 *    3) Skip extra spaces.
 *
 *  Complexity: Time O(n), Space O(n) for split output.
 *  Edge cases: empty after trim (only spaces input) → returns "".
 *  Pattern: tokenize → reverse → join. Foundation for log/word-stream parsing.
 * ============================================================ */
