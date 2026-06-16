package com.company.DSA;

/* ============================================================
 *  LeetCode #345 — Reverse Vowels of a String
 * ============================================================
 *  PROBLEM
 *  -------
 *  Reverse only the vowels in s. Both upper and lowercase.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "hello"     → "holle"
 *  Ex2: "leetcode"  → "leotcede"
 *  Ex3: "aA"        → "Aa"
 *  Ex4: "bcd"       → "bcd"   (no vowels)
 *
 *  CONSTRAINTS:  1 <= s.length <= 3*10^5; ASCII.
 *
 *  HINTS
 *  -----
 *   1. Two pointers from both ends, skip non-vowels, swap when both at vowels.
 * ============================================================ */
public class ReverseVowels {
    public String reverseVowels(String s) {
        char[] a = s.toCharArray();
        int l = 0, r = a.length - 1;
        while (l < r) {
            while (l < r && !isV(a[l])) l++;
            while (l < r && !isV(a[r])) r--;
            char t = a[l]; a[l] = a[r]; a[r] = t;
            l++; r--;
        }
        return new String(a);
    }
    private boolean isV(char c) {
        c = Character.toLowerCase(c);
        return c=='a'||c=='e'||c=='i'||c=='o'||c=='u';
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two-pointer swap restricted to vowel positions. Skip non-vowels on both ends
 *  in inner loops; swap when both pointers see vowels.
 *
 *  Complexity: Time O(n), Space O(n) for char[].
 *  Edge cases: no vowels → unchanged; single char → unchanged.
 *  Pattern: filter-then-two-pointer.
 * ============================================================ */
