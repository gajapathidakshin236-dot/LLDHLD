package com.company.DSA.strings;

/* ============================================================
 *  LeetCode #767 — Reorganize String
 * ============================================================
 *  PROBLEM
 *  -------
 *  Rearrange so no two adjacent characters are the same, or "" if impossible.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "aab"   → "aba"
 *  Ex2: "aaab"  → ""
 *  Ex3: "vvvlo" → "vlvov" (or similar)
 *  Ex4: "a"     → "a"
 *
 *  CONSTRAINTS:  1 <= s.length <= 500.
 *
 *  HINTS
 *  -----
 *   1. Impossible iff max-frequency char > (n+1)/2.
 *   2. Place most frequent char at even indices first, then rest.
 * ============================================================ */
public class ReorganizeString {

    private static final int ALPHABET_SIZE = 26;

    public String reorganizeString(final String input) {
        final int[] frequencyByLetter = countByLetter(input);
        final int   mostFrequentLetterIndex = indexOfMostFrequent(frequencyByLetter);
        final int   maxFrequency = frequencyByLetter[mostFrequentLetterIndex];

        if (maxFrequency > (input.length() + 1) / 2) {
            return "";
        }

        final char[] reordered = new char[input.length()];
        int writeIndex = 0;

        while (frequencyByLetter[mostFrequentLetterIndex]-- > 0) {
            reordered[writeIndex] = (char) ('a' + mostFrequentLetterIndex);
            writeIndex += 2;
        }

        for (int letterIndex = 0; letterIndex < ALPHABET_SIZE; letterIndex++) {
            while (frequencyByLetter[letterIndex]-- > 0) {
                if (writeIndex >= input.length()) {
                    writeIndex = 1;
                }
                reordered[writeIndex] = (char) ('a' + letterIndex);
                writeIndex += 2;
            }
        }
        return new String(reordered);
    }

    private int[] countByLetter(final String input) {
        final int[] counts = new int[ALPHABET_SIZE];
        for (final char character : input.toCharArray()) {
            counts[character - 'a']++;
        }
        return counts;
    }

    private int indexOfMostFrequent(final int[] counts) {
        int maxIndex = 0;
        for (int index = 1; index < counts.length; index++) {
            if (counts[index] > counts[maxIndex]) {
                maxIndex = index;
            }
        }
        return maxIndex;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Place the most frequent char at all even slots first. If even slots are
 *  insufficient (count > ceil(n/2)) → impossible. Then distribute remaining
 *  chars to even slots, wrapping to odd slots — never adjacent to themselves.
 *
 *  Complexity: Time O(n), Space O(1) (bounded alphabet).
 *  Pattern: greedy scheduling.
 * ============================================================ */
