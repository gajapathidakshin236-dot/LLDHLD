package com.company.DSA;

/* ============================================================
 *  LeetCode #5 — Longest Palindromic Substring
 * ============================================================
 *
 *  PROBLEM
 *  -------
 *  Given a string s, return the longest substring of s that is a palindrome.
 *  A palindrome reads the same forwards and backwards.
 *
 *  EXAMPLES
 *  --------
 *  Example 1:
 *    Input:  s = "babad"
 *    Output: "bab"  (or "aba" — both valid)
 *
 *  Example 2:
 *    Input:  s = "cbbd"
 *    Output: "bb"   (even-length center)
 *
 *  Example 3:
 *    Input:  s = "a"
 *    Output: "a"
 *
 *  Example 4:
 *    Input:  s = "forgeeksskeegfor"
 *    Output: "geeksskeeg"
 *
 *  CONSTRAINTS
 *  -----------
 *   1 <= s.length <= 1000
 *   s consists of digits and English letters.
 *
 *  HINTS
 *  -----
 *   1. A palindrome has a center. There are 2n-1 possible centers:
 *      n single-char centers + (n-1) between-char centers.
 *   2. From each center, expand while characters on both sides match.
 *   3. Keep the longest found.
 * ============================================================ */
public class LongestPalindromicSubstring {
    public String longestPalindrome(String s) {
        if (s == null || s.isEmpty()) return "";
        int start = 0, maxLen = 1;
        for (int i = 0; i < s.length(); i++) {
            int l1 = expand(s, i, i);       // odd-length center
            int l2 = expand(s, i, i + 1);   // even-length center
            int len = Math.max(l1, l2);
            if (len > maxLen) {
                maxLen = len;
                start = i - (len - 1) / 2;
            }
        }
        return s.substring(start, start + maxLen);
    }
    private int expand(String s, int l, int r) {
        while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) { l--; r++; }
        return r - l - 1;
    }
}

/* ============================================================
 *  APPROACH EXPLAINED
 * ============================================================
 *
 *  Why "expand around center" works:
 *    Every palindromic substring has a center. Either a single char ("aba" centered at 'b'),
 *    or a gap between two chars ("bb" centered between the two b's). So if we try
 *    every possible center and expand outward, we are guaranteed to discover the
 *    longest palindrome at least once.
 *
 *  Step-by-step:
 *    1. For each index i:
 *         - expand(i, i)     covers odd-length palindromes centered at i.
 *         - expand(i, i+1)   covers even-length palindromes centered between i, i+1.
 *    2. expand returns r - l - 1 — the length AT the moment the mismatch occurred,
 *       because l and r have already stepped one past the matching range.
 *    3. Track best start/length using:
 *         start = i - (len - 1) / 2
 *       which handles both odd and even cases cleanly.
 *
 *  Why not pure DP?
 *    DP table dp[i][j] = "is s[i..j] palindrome?" is O(n^2) time AND O(n^2) space.
 *    Expand-around-center is O(n^2) time but O(1) space — simpler, less memory.
 *
 *  Why not Manacher's (O(n))?
 *    It's O(n) but conceptually heavier. For interview, expand-around-center is
 *    the canonical answer; mention Manacher's as the optimal solution.
 *
 *  Complexity:
 *    Time:  O(n^2) — n centers, each expanding up to n.
 *    Space: O(1)
 *
 *  Edge cases:
 *    - Empty string → return "".
 *    - Length 1 → returns the char itself.
 *    - All same chars ("aaaa") → returns the whole string.
 *
 *  Pattern:
 *    "Expand around center" — works for: count palindromic substrings (#647),
 *    find all palindromic partitionings (#131), etc.
 * ============================================================ */
