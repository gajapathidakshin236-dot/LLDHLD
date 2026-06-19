package com.company.DSA.strings;

import java.util.*;
import java.util.stream.Collectors;

/* ============================================================
 *  LeetCode #49 — Group Anagrams
 * ============================================================
 *  PROBLEM
 *  -------
 *  Group strings that are anagrams of each other.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: ["eat","tea","tan","ate","nat","bat"] → [["bat"],["nat","tan"],["ate","eat","tea"]]
 *  Ex2: [""]   → [[""]]
 *  Ex3: ["a"]  → [["a"]]
 *
 *  CONSTRAINTS:  1 <= strs.length <= 10^4; 0 <= strs[i].length <= 100.
 *
 *  HINTS
 *  -----
 *   1. Two anagrams share the same sorted-char signature.
 *   2. Use HashMap<signature, List<String>>.
 * ============================================================ */
public class GroupAnagrams {

    public List<List<String>> groupAnagrams(final String[] inputs) {
        return new ArrayList<>(
                Arrays.stream(inputs)
                      .collect(Collectors.groupingBy(this::canonicalSignature))
                      .values());
    }

    private String canonicalSignature(final String word) {
        final char[] letters = word.toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Anagrams = same letter multiset. Sorting the characters gives a unique
 *  canonical key. groupingBy(signature) builds the buckets.
 *
 *  Complexity: Time O(n * k log k), Space O(n * k).
 *  Pattern: canonicalize then group via HashMap (or Collectors.groupingBy).
 * ============================================================ */
