package com.company.DSA;

import java.util.*;

/**
 * LeetCode #784 - Letter Case Permutation
 * DFS: at each letter branch lower/upper; digits stay.
 * Time: O(n * 2^L)  Space: O(n)
 */
public class LetterCasePermutation {
    public List<String> letterCasePermutation(String s) {
        List<String> res = new ArrayList<>();
        dfs(s.toCharArray(), 0, res);
        return res;
    }
    private void dfs(char[] a, int i, List<String> res) {
        if (i == a.length) { res.add(new String(a)); return; }
        dfs(a, i + 1, res);
        if (Character.isLetter(a[i])) {
            a[i] ^= 32;       // toggle case
            dfs(a, i + 1, res);
            a[i] ^= 32;
        }
    }
}
