package com.company.DSA;

/* ============================================================
 *  LeetCode #28 — Find the Index of the First Occurrence in a String (strStr)
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the index of the first occurrence of needle in haystack, or -1.
 *  If needle is empty, return 0 (conventional).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: haystack="sadbutsad", needle="sad" → 0
 *  Ex2: haystack="leetcode", needle="leeto" → -1
 *  Ex3: haystack="hello",   needle="ll"    → 2
 *  Ex4: haystack="abc",     needle=""      → 0
 *
 *  CONSTRAINTS:  1 <= haystack.length, needle.length <= 10^4; lowercase English.
 *
 *  HINTS
 *  -----
 *   1. Brute force: try every start position; compare char-by-char.
 *   2. KMP / Z-algorithm gives O(n+m) for huge inputs.
 *   3. For the constraints here, brute is fine.
 * ============================================================ */
public class FindFirstOccurrence {
    public int strStr(String h, String n) {
        int H = h.length(), N = n.length();
        if (N == 0) return 0;
        for (int i = 0; i <= H - N; i++) {
            int j = 0;
            while (j < N && h.charAt(i + j) == n.charAt(j)) j++;
            if (j == N) return i;
        }
        return -1;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Sliding compare. For each candidate start `i` from 0 to H-N, compare needle
 *  characters one by one. Stop at first full match.
 *
 *  Why H - N (not H):
 *    Beyond that point, not enough characters remain to match needle.
 *
 *  Upgrade — KMP:
 *    Precompute LPS array; never re-check characters → O(n+m).
 *
 *  Complexity: Time O(H*N) worst, O(H+N) with KMP.
 *  Edge cases: empty needle → 0; needle longer than haystack → -1.
 *  Pattern: pattern matching. KMP, Rabin-Karp, Z-algo all live here.
 * ============================================================ */
