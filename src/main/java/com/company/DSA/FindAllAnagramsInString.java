package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #438 — Find All Anagrams in a String
 * ============================================================
 *
 *  PROBLEM
 *  -------
 *  Given two strings s and p, return all the start indices of p's anagrams in s.
 *  You may return the answer in any order.
 *
 *  EXAMPLES
 *  --------
 *  Example 1:
 *    Input:  s = "cbaebabacd", p = "abc"
 *    Output: [0, 6]
 *    Why:    s[0..2]="cba" and s[6..8]="bac" are anagrams of "abc".
 *  Example 2:
 *    Input:  s = "abab", p = "ab"
 *    Output: [0, 1, 2]
 *  Example 3:
 *    Input:  s = "af", p = "be"
 *    Output: []  (no anagrams)
 *  Example 4:
 *    Input:  s = "aa", p = "bb"
 *    Output: []
 *
 *  CONSTRAINTS
 *  -----------
 *   1 <= s.length, p.length <= 3 * 10^4
 *   Lowercase English letters.
 *
 *  HINTS
 *  -----
 *   1. Anagrams = identical letter-count signature.
 *   2. Use a fixed-size sliding window of length |p|.
 *   3. Update window count by +1 on the right edge, -1 on the left edge.
 *   4. Compare the window's count to p's count.
 * ============================================================ */
public class FindAllAnagramsInString {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s.length() < p.length()) return res;
        int[] pc = new int[26], wc = new int[26];
        for (char c : p.toCharArray()) pc[c - 'a']++;
        int k = p.length();
        for (int i = 0; i < s.length(); i++) {
            wc[s.charAt(i) - 'a']++;
            if (i >= k) wc[s.charAt(i - k) - 'a']--;
            if (Arrays.equals(pc, wc)) res.add(i - k + 1);
        }
        return res;
    }
}

/* ============================================================
 *  APPROACH EXPLAINED
 * ============================================================
 *
 *  Why fixed-size sliding window:
 *    Anagrams of p must have the SAME LENGTH as p. So we only ever need
 *    windows of length k=|p|. We slide a window of that fixed size and check
 *    each one.
 *
 *  Why letter-count, not sorted string:
 *    Each window comparison via sorted string is O(k log k). Letter-count
 *    comparison is O(26) = O(1).
 *
 *  Step-by-step:
 *    1. Build pc[] = count of each letter in p.
 *    2. Walk i over s, maintaining wc[] = count of letters in window [i-k+1..i].
 *       - On entry: wc[s[i]]++.
 *       - On exit:  if i >= k, wc[s[i-k]]-- to slide left edge.
 *    3. Once window is full (i >= k-1), compare wc to pc; if equal, record start.
 *
 *  Optimization (matches counter):
 *    Instead of Arrays.equals (O(26)) per step, maintain `matches` counter:
 *      - when wc[c] becomes equal to pc[c], matches++
 *      - when it leaves equality, matches--
 *    Window valid when matches == 26. Same complexity but smaller constant.
 *
 *  Complexity:
 *    Time:  O(n) — each char enters & leaves window once. 26-char compare is O(1).
 *    Space: O(1) — two fixed 26-int arrays.
 *
 *  Edge cases:
 *    - |s| < |p| → no answer.
 *    - p is single char → return every index where s[i] == p[0].
 *    - Repeated letters in p (e.g., "aab") → count vector still correct.
 *
 *  Pattern:
 *    "Fixed-size sliding window + frequency counter." Same pattern:
 *    permutation in string (#567), substring with same chars, etc.
 * ============================================================ */
