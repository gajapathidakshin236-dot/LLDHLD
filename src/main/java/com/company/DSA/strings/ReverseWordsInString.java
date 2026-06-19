package com.company.DSA.strings;

import java.util.*;
import java.util.stream.*;

/* ============================================================
 *  LeetCode #151 — Reverse Words in a String
 * ============================================================
 *  PROBLEM
 *  -------
 *  Reverse the order of words, collapsing repeated whitespace and trimming.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "the sky is blue"        → "blue is sky the"
 *  Ex2: "  hello world  "        → "world hello"
 *  Ex3: "a good   example"       → "example good a"
 *
 *  CONSTRAINTS:  1 <= s.length <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. trim() + split("\\s+") gives clean words.
 *   2. Reverse the array of words and join with single space.
 * ============================================================ */
public class ReverseWordsInString {

    public String reverseWords(final String input) {
        final String[] words = input.trim().split("\\s+");
        final List<String> reversedWords = new ArrayList<>(Arrays.asList(words));
        Collections.reverse(reversedWords);
        return reversedWords.stream().collect(Collectors.joining(" "));
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Tokenize → reverse → join. trim() strips edges; split("\\s+") collapses
 *  runs of whitespace; Collectors.joining glues them back.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: tokenize → reverse → join.
 * ============================================================ */
