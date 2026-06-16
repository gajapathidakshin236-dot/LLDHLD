package com.company.DSA;

/* ============================================================
 *  LeetCode #1208 — Get Equal Substrings Within Budget
 * ============================================================
 *  PROBLEM
 *  -------
 *  Convert s[i] to t[i] at cost |s[i]-t[i]|. Find the maximum length contiguous
 *  substring such that total conversion cost <= maxCost.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: s="abcd", t="bcdf", maxCost=3 → 3   (last 3 chars cost 1+1+1=3)
 *  Ex2: s="abcd", t="cdef", maxCost=3 → 1
 *  Ex3: s="abcd", t="acde", maxCost=0 → 1   (length-1 windows are free)
 *
 *  CONSTRAINTS:  1 <= len <= 10^5; 0 <= maxCost <= 10^6; lowercase English.
 *
 *  HINTS
 *  -----
 *   1. Cost array c[i] = |s[i]-t[i]|. Find longest window with sum <= maxCost.
 *   2. Sliding window with two pointers.
 * ============================================================ */
public class EqualSubstringsBudget {
    public int equalSubstring(String s, String t, int maxCost) {
        int l = 0, sum = 0, best = 0;
        for (int r = 0; r < s.length(); r++) {
            sum += Math.abs(s.charAt(r) - t.charAt(r));
            while (sum > maxCost) sum -= Math.abs(s.charAt(l) - t.charAt(l++));
            best = Math.max(best, r - l + 1);
        }
        return best;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Treat the per-index conversion costs as an array. We need the longest
 *  subarray whose sum <= budget. Sliding window because costs are non-negative.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Edge cases: empty budget; very large budget covering whole string.
 *  Pattern: "max window with sum <= K" — same as #1004, #424.
 * ============================================================ */
