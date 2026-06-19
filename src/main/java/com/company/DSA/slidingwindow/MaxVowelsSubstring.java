package com.company.DSA.slidingwindow;

import java.util.*;

/* ============================================================
 *  LeetCode #1456 — Maximum Number of Vowels in Substring of Given Length
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the max number of vowels in any substring of length k.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: s="abciiidef", k=3 → 3
 *  Ex2: s="aeiou",     k=2 → 2
 *  Ex3: s="leetcode",  k=3 → 2
 *  Ex4: s="rhythms",   k=4 → 0
 *
 *  CONSTRAINTS:  1 <= s.length <= 10^5; 1 <= k <= s.length.
 *
 *  HINTS
 *  -----
 *   1. Fixed-size sliding window; vowel count diff.
 * ============================================================ */
public class MaxVowelsSubstring {

    private static final Set<Character> VOWELS = new HashSet<>(
            Arrays.asList('a','e','i','o','u'));

    public int maxVowels(final String input, final int windowSize) {
        int vowelCountInWindow = 0;
        int maxVowelCountSeen  = 0;

        for (int index = 0; index < input.length(); index++) {
            if (VOWELS.contains(input.charAt(index))) {
                vowelCountInWindow++;
            }
            if (index >= windowSize && VOWELS.contains(input.charAt(index - windowSize))) {
                vowelCountInWindow--;
            }
            if (index >= windowSize - 1) {
                maxVowelCountSeen = Math.max(maxVowelCountSeen, vowelCountInWindow);
            }
        }
        return maxVowelCountSeen;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Fixed-size sliding window with running vowel counter (+1 entering, -1 leaving).
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: fixed window + predicate count.
 * ============================================================ */
