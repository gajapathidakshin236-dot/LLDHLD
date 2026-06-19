package com.company.DSA.strings;

import java.util.*;

/* ============================================================
 *  LeetCode #187 — Repeated DNA Sequences
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return all 10-letter substrings of s that appear MORE THAN ONCE.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT" → ["AAAAACCCCC","CCCCCAAAAA"]
 *  Ex2: "AAAAAAAAAAAAA" → ["AAAAAAAAAA"]
 *  Ex3: "ACGT" → []
 *
 *  CONSTRAINTS:  1 <= s.length <= 10^5.
 *
 *  HINTS
 *  -----
 *   1. Sliding window of 10 chars; seen set + repeats set.
 * ============================================================ */
public class RepeatedDNASequences {

    private static final int WINDOW_LENGTH = 10;

    public List<String> findRepeatedDnaSequences(final String genome) {
        final Set<String> seenSequences     = new HashSet<>();
        final Set<String> repeatedSequences = new HashSet<>();

        for (int startIndex = 0; startIndex + WINDOW_LENGTH <= genome.length(); startIndex++) {
            final String candidate = genome.substring(startIndex, startIndex + WINDOW_LENGTH);
            if (!seenSequences.add(candidate)) {
                repeatedSequences.add(candidate);
            }
        }
        return new ArrayList<>(repeatedSequences);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Slide a window of 10 chars; track every candidate substring in a HashSet.
 *  On insertion failure, the substring is a repeat.
 *
 *  Complexity: Time O(n*10), Space O(n).
 *  Pattern: fixed-window dedup.
 * ============================================================ */
