package com.company.DSA;

import java.util.*;

/**
 * LeetCode #17 - Letter Combinations of a Phone Number
 * Backtrack character by character.
 * Time: O(4^n * n)  Space: O(n)
 */
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
