package com.company.DSA;

import java.util.*;

/**
 * LeetCode #451 - Sort Characters by Frequency
 * Count freq; bucket sort or PriorityQueue.
 * Time: O(n)  Space: O(n)
 */
public class SortCharsByFrequency {
    public String frequencySort(String s) {
        Map<Character, Integer> cnt = new HashMap<>();
        for (char c : s.toCharArray()) cnt.merge(c, 1, Integer::sum);
        List<Character>[] bucket = new List[s.length() + 1];
        for (var e : cnt.entrySet()) {
            int f = e.getValue();
            if (bucket[f] == null) bucket[f] = new ArrayList<>();
            bucket[f].add(e.getKey());
        }
        StringBuilder sb = new StringBuilder();
        for (int f = s.length(); f >= 1; f--) {
            if (bucket[f] == null) continue;
            for (char c : bucket[f]) for (int i = 0; i < f; i++) sb.append(c);
        }
        return sb.toString();
    }
}
