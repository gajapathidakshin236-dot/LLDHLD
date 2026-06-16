package com.company.DSA;

/* ============================================================
 *  LeetCode #76 — Minimum Window Substring
 * ============================================================
 *
 *  PROBLEM
 *  -------
 *  Given strings s and t, return the minimum window substring of s such that
 *  every character in t (including duplicates) is included. If no such window
 *  exists, return "".
 *
 *  EXAMPLES
 *  --------
 *  Example 1:
 *    Input:  s = "ADOBECODEBANC", t = "ABC"
 *    Output: "BANC"
 *  Example 2:
 *    Input:  s = "a", t = "a"
 *    Output: "a"
 *  Example 3:
 *    Input:  s = "a", t = "aa"
 *    Output: ""    (need two 'a's, only one available)
 *  Example 4:
 *    Input:  s = "ab", t = "b"
 *    Output: "b"
 *
 *  CONSTRAINTS
 *  -----------
 *   1 <= s.length, t.length <= 10^5
 *   s and t consist of upper/lowercase English letters.
 *
 *  HINTS
 *  -----
 *   1. Variable-size sliding window. Expand r until window covers t, then
 *      shrink l while still covered, recording best.
 *   2. Track per-char "need" counts and a "formed" counter of distinct chars satisfied.
 *   3. Only when a char's window count EQUALS its need do we increment formed.
 * ============================================================ */
public class MinimumWindowSubstring {
    public String minWindow(String s, String t) {
        if (s.length() < t.length()) return "";
        int[] need = new int[128];
        int required = 0;
        for (char c : t.toCharArray()) { if (need[c]++ == 0) required++; }

        int l = 0, formed = 0, bestL = 0, bestLen = Integer.MAX_VALUE;
        int[] window = new int[128];
        for (int r = 0; r < s.length(); r++) {
            char c = s.charAt(r);
            window[c]++;
            if (need[c] > 0 && window[c] == need[c]) formed++;
            while (formed == required) {
                if (r - l + 1 < bestLen) { bestLen = r - l + 1; bestL = l; }
                char lc = s.charAt(l++);
                window[lc]--;
                if (need[lc] > 0 && window[lc] < need[lc]) formed--;
            }
        }
        return bestLen == Integer.MAX_VALUE ? "" : s.substring(bestL, bestL + bestLen);
    }
}

/* ============================================================
 *  APPROACH EXPLAINED
 * ============================================================
 *
 *  Why sliding window:
 *    We want the SHORTEST contiguous range. Expanding r until valid, then
 *    shrinking l while still valid, lets every char be visited at most twice.
 *
 *  Required / formed mechanic:
 *    - `required` = number of DISTINCT chars in t (e.g., t="AABC" → required=3).
 *    - `formed`   = how many of those distinct chars are currently SATISFIED in the window.
 *    A char is "satisfied" the exact moment window[c] equals need[c]. Going past
 *    that doesn't make it more satisfied (already counted) — that's why we use ==.
 *    Window valid ⇔ formed == required.
 *
 *  Step-by-step:
 *    1. Build need[] from t and compute required.
 *    2. Expand r:
 *         window[c]++; if (need[c] > 0 && window[c] == need[c]) formed++;
 *    3. While window is valid, record best length, then shrink:
 *         window[lc]--; if (need[lc] > 0 && window[lc] < need[lc]) formed--;
 *    4. Return best substring or "".
 *
 *  Why int[128]?
 *    ASCII range 0..127 covers upper+lower English. Faster than HashMap.
 *
 *  Complexity:
 *    Time:  O(|s| + |t|)  — each char enters and leaves window once.
 *    Space: O(1)          — bounded alphabet.
 *
 *  Edge cases:
 *    - |s| < |t| → return "" immediately.
 *    - t contains repeated chars (e.g. "AABC") → need counts handle it.
 *    - No window covers t → bestLen never updated → return "".
 *
 *  Pattern:
 *    "Variable-size sliding window with required/formed counter." Same idea:
 *    longest substring with at most K distinct (#340), permutation in string (#567).
 * ============================================================ */
