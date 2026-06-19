package com.company.DSA.strings;

import java.util.*;

/* ============================================================
 *  LeetCode #451 — Sort Characters by Frequency
 * ============================================================
 *  PROBLEM
 *  -------
 *  Sort the string so characters with HIGHER frequency come first.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "tree"   → "eert"
 *  Ex2: "cccaaa" → "aaaccc" or "cccaaa"
 *  Ex3: "Aabb"   → "bbAa" or "bbaA"
 *
 *  CONSTRAINTS:  1 <= n <= 5*10^5; printable ASCII.
 *
 *  HINTS
 *  -----
 *   1. Count with HashMap.
 *   2. Bucket-sort by frequency for O(n).
 * ============================================================ */
public class SortCharsByFrequency {

    public String frequencySort(final String input) {
        final Map<Character, Integer> frequencyByChar = countCharacters(input);

        @SuppressWarnings("unchecked")
        final List<Character>[] bucketsByFrequency = new List[input.length() + 1];

        for (final Map.Entry<Character, Integer> entry : frequencyByChar.entrySet()) {
            final int frequency = entry.getValue();
            bucketsByFrequency[frequency] =
                    bucketsByFrequency[frequency] != null
                            ? bucketsByFrequency[frequency]
                            : new ArrayList<>();
            bucketsByFrequency[frequency].add(entry.getKey());
        }

        final StringBuilder result = new StringBuilder(input.length());
        for (int frequency = input.length(); frequency >= 1; frequency--) {
            if (bucketsByFrequency[frequency] == null) {
                continue;
            }
            for (final char character : bucketsByFrequency[frequency]) {
                for (int copy = 0; copy < frequency; copy++) {
                    result.append(character);
                }
            }
        }
        return result.toString();
    }

    private Map<Character, Integer> countCharacters(final String input) {
        final Map<Character, Integer> frequencyByChar = new HashMap<>();
        for (final char character : input.toCharArray()) {
            frequencyByChar.merge(character, 1, Integer::sum);
        }
        return frequencyByChar;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Bucket sort by frequency. Max frequency is s.length(), so a fixed array of
 *  (s.length()+1) buckets works. Iterate from highest to lowest, emit each
 *  character `f` times.
 *
 *  Complexity: Time O(n + A), Space O(n + A).
 *  Pattern: bucket sort over bounded frequency.
 * ============================================================ */
