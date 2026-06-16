package com.company.DSA;

/* ============================================================
 *  LeetCode #13 — Roman to Integer
 * ============================================================
 *  PROBLEM
 *  -------
 *  Convert a valid Roman numeral string to an integer in [1, 3999].
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "III"     → 3
 *  Ex2: "IV"      → 4
 *  Ex3: "IX"      → 9
 *  Ex4: "LVIII"   → 58
 *  Ex5: "MCMXCIV" → 1994
 *
 *  CONSTRAINTS:  1 <= s.length <= 15; valid Roman.
 *
 *  HINTS
 *  -----
 *   1. Walk left to right. If current < next → SUBTRACT current; else add it.
 *   2. Lookup table for values keyed by char.
 * ============================================================ */
public class RomanToInteger {
    public int romanToInt(String s) {
        int[] v = new int[128];
        v['I']=1; v['V']=5; v['X']=10; v['L']=50; v['C']=100; v['D']=500; v['M']=1000;
        int res = 0, n = s.length();
        for (int i = 0; i < n; i++) {
            int cur = v[s.charAt(i)];
            int nxt = (i + 1 < n) ? v[s.charAt(i + 1)] : 0;
            res += (cur < nxt) ? -cur : cur;
        }
        return res;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Single left-to-right pass with one-character lookahead:
 *    cur < nxt → cur is the "subtractive" part (e.g. I before V) → -cur
 *    else      → +cur
 *  Out of bounds → treat next as 0.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Edge cases: single char ("I", "M"); end of string lookahead returns 0.
 *  Pattern: one-step lookahead transformation.
 * ============================================================ */
