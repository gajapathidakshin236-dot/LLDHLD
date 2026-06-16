package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #49 — Group Anagrams
 * ============================================================
 *
 *  PROBLEM
 *  -------
 *  Given an array of strings strs, group the anagrams together. An anagram
 *  is a word formed by rearranging the letters of another. Return the groups
 *  in any order.
 *
 *  EXAMPLES
 *  --------
 *  Example 1:
 *    Input:  ["eat","tea","tan","ate","nat","bat"]
 *    Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
 *
 *  Example 2:
 *    Input:  [""]
 *    Output: [[""]]
 *
 *  Example 3:
 *    Input:  ["a"]
 *    Output: [["a"]]
 *
 *  CONSTRAINTS
 *  -----------
 *   1 <= strs.length <= 10^4
 *   0 <= strs[i].length <= 100
 *   strs[i] is lowercase English letters.
 *
 *  HINTS
 *  -----
 *   1. Two strings are anagrams iff their sorted forms are identical.
 *   2. Use sorted string as the HashMap key; values are the group.
 *   3. Alternative key — letter-count signature like "a2b1c3..." → avoids sort.
 * ============================================================ */
public class GroupAnagrams {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            char[] c = s.toCharArray();
            Arrays.sort(c);
            String key = new String(c);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }
        return new ArrayList<>(map.values());
    }
}

/* ============================================================
 *  APPROACH EXPLAINED
 * ============================================================
 *
 *  Why this works:
 *    "Anagram" = same letter multiset. Two simple canonical forms exist:
 *      (a) sort the letters → identical strings ↔ anagrams.
 *      (b) frequency vector of 26 letters → identical vectors ↔ anagrams.
 *    Pick one as the HashMap key.
 *
 *  Step-by-step (sorted-key version):
 *    1. For each string s, sort its chars to form a key.
 *    2. computeIfAbsent ensures we lazily create the bucket only if missing.
 *    3. Append s to that bucket.
 *    4. Return all bucket values.
 *
 *  Counting-key alternative (faster for very long strings):
 *    int[] cnt = new int[26];
 *    for (char c : s.toCharArray()) cnt[c-'a']++;
 *    String key = Arrays.toString(cnt); // or build "a2b1..."
 *    Time becomes O(n * k) instead of O(n * k log k).
 *
 *  Why HashMap over sorting the whole input?
 *    Sorting whole input is O(n*k log n) and complicates grouping.
 *    HashMap is cleaner and faster.
 *
 *  Complexity:
 *    Time:  O(n * k log k) with sorted key (k = avg string length).
 *           O(n * k) with counting key.
 *    Space: O(n * k) total stored.
 *
 *  Edge cases:
 *    - Empty strings → they share key "" and group together.
 *    - All distinct words → every group is a singleton.
 *    - Identical strings → still anagrams of each other (same group).
 *
 *  Pattern:
 *    "Canonicalize then group by HashMap." Useful in: group shifted strings
 *    (#249), find duplicates by signature, deduping near-duplicate records.
 * ============================================================ */
