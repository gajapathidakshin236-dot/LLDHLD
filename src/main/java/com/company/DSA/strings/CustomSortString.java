package com.company.DSA.strings;

/* ============================================================
 *  LeetCode #791 — Custom Sort String
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return any permutation of s such that characters appear in the relative
 *  order defined by `order`.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: order="cba", s="abcd" → "cbad"
 *  Ex2: order="cbafg", s="abcd" → "cbad"
 *  Ex3: order="", s="xyz" → any permutation of "xyz"
 *
 *  CONSTRAINTS:  1 <= order.length, s.length <= 200; lowercase English.
 *
 *  HINTS
 *  -----
 *   1. Count each char in s; emit chars per order's sequence; append remainder.
 * ============================================================ */
public class CustomSortString {

    private static final int ALPHABET_SIZE = 26;

    public String customSortString(final String orderHint, final String input) {
        final int[] frequencyByLetter = new int[ALPHABET_SIZE];
        for (final char character : input.toCharArray()) {
            frequencyByLetter[character - 'a']++;
        }

        final StringBuilder reordered = new StringBuilder(input.length());

        for (final char character : orderHint.toCharArray()) {
            final int index = character - 'a';
            while (frequencyByLetter[index]-- > 0) {
                reordered.append(character);
            }
        }
        for (int index = 0; index < ALPHABET_SIZE; index++) {
            while (frequencyByLetter[index]-- > 0) {
                reordered.append((char) ('a' + index));
            }
        }
        return reordered.toString();
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Counting sort with custom alphabet order. First emit by order's sequence;
 *  then dump leftover chars.
 *
 *  Complexity: Time O(|s| + |order|), Space O(1) (fixed alphabet).
 *  Pattern: bucket / counting sort with custom comparator.
 * ============================================================ */
