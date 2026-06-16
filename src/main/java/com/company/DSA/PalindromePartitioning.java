package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #131 — Palindrome Partitioning
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given s, return all ways to partition s into contiguous substrings each
 *  of which is a palindrome.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "aab" → [["a","a","b"],["aa","b"]]
 *  Ex2: "a"   → [["a"]]
 *  Ex3: "abc" → [["a","b","c"]]
 *  Ex4: "aaa" → [["a","a","a"],["a","aa"],["aa","a"],["aaa"]]
 *
 *  CONSTRAINTS:  1 <= s.length <= 16; lowercase English.
 *
 *  HINTS
 *  -----
 *   1. Backtrack. For each prefix that is a palindrome, recurse on the rest.
 *   2. isPalindrome can be O(n) check; total still bounded by 2^n.
 *   3. Optionally precompute isPal[i][j] via DP for speed.
 * ============================================================ */
public class PalindromePartitioning {
    public List<List<String>> partition(String s) {
        List<List<String>> res = new ArrayList<>();
        bt(s, 0, new ArrayList<>(), res);
        return res;
    }
    private void bt(String s, int i, List<String> cur, List<List<String>> res) {
        if (i == s.length()) { res.add(new ArrayList<>(cur)); return; }
        for (int j = i; j < s.length(); j++) {
            if (isPal(s, i, j)) {
                cur.add(s.substring(i, j + 1));
                bt(s, j + 1, cur, res);
                cur.remove(cur.size() - 1);
            }
        }
    }
    private boolean isPal(String s, int l, int r) {
        while (l < r) { if (s.charAt(l++) != s.charAt(r--)) return false; }
        return true;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Standard backtracking: at index i, try every j >= i. If s[i..j] is a
 *  palindrome, append it and recurse from j+1. Snapshot on i == n.
 *
 *  DP speedup:
 *    Precompute isPal[i][j] in O(n^2) using bottom-up:
 *      isPal[i][j] = (s[i]==s[j]) && (j-i<2 || isPal[i+1][j-1])
 *
 *  Complexity: Time O(n * 2^n), Space O(n) recursion.
 *  Pattern: backtracking + decision-per-split. Same: word break (#139, #140).
 * ============================================================ */
