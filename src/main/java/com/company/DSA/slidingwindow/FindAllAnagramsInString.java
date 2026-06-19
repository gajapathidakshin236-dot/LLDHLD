package com.company.DSA.slidingwindow;

import java.util.*;

/* ============================================================
 *  LeetCode #438 — Find All Anagrams in a String
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return all starting indices of substrings in s that are anagrams of p.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: s="cbaebabacd", p="abc" → [0, 6]
 *  Ex2: s="abab", p="ab" → [0, 1, 2]
 *  Ex3: s="af", p="be" → []
 *
 *  CONSTRAINTS:  1 <= s.length, p.length <= 3*10^4.
 *
 *  HINTS
 *  -----
 *   1. Fixed-size sliding window of length |p|.
 *   2. Compare letter-frequency vectors.
 * ============================================================ */
public class FindAllAnagramsInString {

    private static final int ALPHABET_SIZE = 26;

    public List<Integer> findAnagrams(final String source, final String pattern) {
        final List<Integer> matchStartIndices = new ArrayList<>();
        if (source.length() < pattern.length()) {
            return matchStartIndices;
        }

        final int[] patternCounts = new int[ALPHABET_SIZE];
        final int[] windowCounts  = new int[ALPHABET_SIZE];
        for (final char character : pattern.toCharArray()) {
            patternCounts[character - 'a']++;
        }

        final int windowSize = pattern.length();

        for (int index = 0; index < source.length(); index++) {
            windowCounts[source.charAt(index) - 'a']++;

            if (index >= windowSize) {
                windowCounts[source.charAt(index - windowSize) - 'a']--;
            }

            if (Arrays.equals(patternCounts, windowCounts)) {
                matchStartIndices.add(index - windowSize + 1);
            }
        }
        return matchStartIndices;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Fixed-size sliding window of length |p|. Maintain letter counts; compare to
 *  pattern's counts each step.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: fixed-size sliding window + frequency counter.
 * ============================================================ */
