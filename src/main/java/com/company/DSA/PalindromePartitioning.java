package com.company.DSA;

import java.util.*;

/**
 * LeetCode #131 - Palindrome Partitioning
 * Backtrack: for each prefix that's a palindrome, recurse on rest.
 * Time: O(n * 2^n)  Space: O(n)
 */
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
