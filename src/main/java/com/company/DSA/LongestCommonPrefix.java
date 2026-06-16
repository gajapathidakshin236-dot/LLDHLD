package com.company.DSA;

/* ============================================================
 *  LeetCode #14 — Longest Common Prefix
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the longest common prefix among an array of strings.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: ["flower","flow","flight"] → "fl"
 *  Ex2: ["dog","racecar","car"]    → ""
 *  Ex3: ["interspecies","interstellar","interstate"] → "inters"
 *  Ex4: ["a"]                       → "a"
 *
 *  CONSTRAINTS:  1 <= n <= 200;  0 <= str.length <= 200; lowercase letters.
 *
 *  HINTS
 *  -----
 *   1. Vertical scan: compare characters at column i across all strings.
 *   2. Stop at first mismatch OR when any string ends.
 *   3. Alternative: sort the array; compare only first & last lexicographically.
 * ============================================================ */
public class LongestCommonPrefix {
    public String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) return "";
        for (int i = 0; i < strs[0].length(); i++) {
            char c = strs[0].charAt(i);
            for (int j = 1; j < strs.length; j++) {
                if (i == strs[j].length() || strs[j].charAt(i) != c) return strs[0].substring(0, i);
            }
        }
        return strs[0];
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Vertical scan. Use first string as a reference; at each column i, verify
 *  every other string matches. Stop at the first column that fails (or runs out).
 *
 *  Sort-trick alt (one-liner):
 *    Sort lexicographically; compare strs[0] with strs[n-1] character-by-character.
 *    The "weakest" common prefix is determined by extremes.
 *
 *  Complexity: Time O(S) where S = total characters. Space O(1).
 *  Edge cases: single string → returns itself; empty list (defensive) → "".
 *  Pattern: column-wise scan.
 * ============================================================ */
