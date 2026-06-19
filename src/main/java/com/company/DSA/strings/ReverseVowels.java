package com.company.DSA.strings;

import java.util.*;

/* ============================================================
 *  LeetCode #345 — Reverse Vowels of a String
 * ============================================================
 *  PROBLEM
 *  -------
 *  Reverse only the vowels in s (upper or lowercase).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "hello"    → "holle"
 *  Ex2: "leetcode" → "leotcede"
 *  Ex3: "aA"       → "Aa"
 *  Ex4: "bcd"      → "bcd"
 *
 *  CONSTRAINTS:  1 <= s.length <= 3*10^5.
 *
 *  HINTS
 *  -----
 *   1. Two pointers, skip non-vowels, swap when both at vowels.
 * ============================================================ */
public class ReverseVowels {

    private static final Set<Character> VOWELS = new HashSet<>(
            Arrays.asList('a','e','i','o','u','A','E','I','O','U'));

    public String reverseVowels(final String input) {
        final char[] characters = input.toCharArray();
        int leftCursor  = 0;
        int rightCursor = characters.length - 1;

        while (leftCursor < rightCursor) {
            while (leftCursor < rightCursor && !VOWELS.contains(characters[leftCursor])) {
                leftCursor++;
            }
            while (leftCursor < rightCursor && !VOWELS.contains(characters[rightCursor])) {
                rightCursor--;
            }
            swap(characters, leftCursor++, rightCursor--);
        }
        return new String(characters);
    }

    private void swap(final char[] characters, final int firstIndex, final int secondIndex) {
        final char temp = characters[firstIndex];
        characters[firstIndex]  = characters[secondIndex];
        characters[secondIndex] = temp;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two-pointer swap restricted to vowel positions.
 *
 *  Complexity: Time O(n), Space O(n) for char[].
 *  Pattern: filter-then-two-pointer.
 * ============================================================ */
