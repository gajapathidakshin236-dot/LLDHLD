package com.company.DSA.strings;

/* ============================================================
 *  LeetCode #28 — Find the Index of the First Occurrence (strStr)
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the index of the first occurrence of needle in haystack, or -1.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: haystack="sadbutsad", needle="sad" → 0
 *  Ex2: haystack="leetcode", needle="leeto" → -1
 *  Ex3: haystack="hello", needle="ll" → 2
 *  Ex4: haystack="abc", needle="" → 0
 *
 *  CONSTRAINTS:  1 <= length <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Brute compare at each candidate start.
 *   2. KMP / Z give O(n+m) for large inputs.
 * ============================================================ */
public class FindFirstOccurrence {

    public int strStr(final String haystack, final String needle) {
        final int haystackLength = haystack.length();
        final int needleLength   = needle.length();

        if (needleLength == 0) {
            return 0;
        }

        for (int candidateStart = 0; candidateStart <= haystackLength - needleLength; candidateStart++) {
            if (matchesAt(haystack, candidateStart, needle)) {
                return candidateStart;
            }
        }
        return -1;
    }

    private boolean matchesAt(final String haystack, final int startIndex, final String needle) {
        for (int offset = 0; offset < needle.length(); offset++) {
            if (haystack.charAt(startIndex + offset) != needle.charAt(offset)) {
                return false;
            }
        }
        return true;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Try each candidate start position; compare needle char-by-char.
 *
 *  Complexity: Time O(H*N) worst-case, Space O(1).
 *  Pattern: substring search.
 * ============================================================ */
