package com.company.DSA.strings;

/* ============================================================
 *  LeetCode #880 — Decoded String at Index
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the k-th character of the fully decoded string without actually
 *  expanding it.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: s="leet2code3", k=10 → "o"
 *  Ex2: s="ha22",       k=5  → "h"
 *  Ex3: s="a2b3c4d5e6f7g8h9", k=10 → "c"
 *
 *  CONSTRAINTS:  2 <= s.length <= 100.
 *
 *  HINTS
 *  -----
 *   1. Compute total decoded LENGTH first.
 *   2. Walk back; k modulo size; on letter at matched k → answer.
 * ============================================================ */
public class DecodedStringAtIndex {

    public String decodeAtIndex(final String encoded, int kthCharIndex) {
        long decodedLength = computeDecodedLength(encoded);

        for (int cursor = encoded.length() - 1; cursor >= 0; cursor--) {
            final char character = encoded.charAt(cursor);
            kthCharIndex = (int) (kthCharIndex % decodedLength);

            if (kthCharIndex == 0 && Character.isLetter(character)) {
                return String.valueOf(character);
            }
            if (Character.isDigit(character)) {
                decodedLength /= (character - '0');
            } else {
                decodedLength--;
            }
        }
        return "";
    }

    private long computeDecodedLength(final String encoded) {
        long length = 0;
        for (final char character : encoded.toCharArray()) {
            length = Character.isDigit(character)
                    ? length * (character - '0')
                    : length + 1;
        }
        return length;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Compute the (huge) decoded length first. Walk RIGHT to LEFT, shrinking k
 *  via modulo so finding the k-th char in the long decoded string reduces
 *  to a smaller k in the current segment.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: lazy / on-demand decoding via reverse traversal.
 * ============================================================ */
