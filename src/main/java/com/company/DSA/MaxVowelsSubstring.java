package com.company.DSA;

/* ============================================================
 *  LeetCode #1456 — Maximum Number of Vowels in a Substring of Given Length
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given string s and integer k, return the maximum number of vowel letters
 *  (a, e, i, o, u) in any substring of s of length k.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: s="abciiidef", k=3 → 3   ("iii")
 *  Ex2: s="aeiou",     k=2 → 2
 *  Ex3: s="leetcode",  k=3 → 2   ("eet")
 *  Ex4: s="rhythms",   k=4 → 0   (no vowels)
 *
 *  CONSTRAINTS:  1 <= s.length <= 10^5;  1 <= k <= s.length.
 *
 *  HINTS
 *  -----
 *   1. Fixed-size sliding window of length k.
 *   2. Maintain a running vowel count: +1 on entering char, -1 on leaving.
 *   3. Update best when window has reached size k.
 * ============================================================ */
public class MaxVowelsSubstring {
    public int maxVowels(String s, int k) {
        int cnt = 0, best = 0;
        for (int i = 0; i < s.length(); i++) {
            if (isV(s.charAt(i))) cnt++;
            if (i >= k && isV(s.charAt(i - k))) cnt--;
            if (i >= k - 1) best = Math.max(best, cnt);
        }
        return best;
    }
    private boolean isV(char c) { return c=='a'||c=='e'||c=='i'||c=='o'||c=='u'; }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Fixed-size sliding window. Each step:
 *    - +1 if entering char is vowel.
 *    - -1 if leaving char (index i-k) is vowel.
 *    - Once window has formed (i >= k-1), update best.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Edge cases: k=1 → just check each char; k=n → count all vowels.
 *  Pattern: fixed window + per-char predicate count. Same idea: counting any
 *           character-class in length-k windows.
 * ============================================================ */
