package com.company.DSA;

import java.util.*;

/**
 * LeetCode #187 - Repeated DNA Sequences
 * Sliding window of 10 chars; track seen + result sets.
 * Time: O(n)  Space: O(n)
 */
public class RepeatedDNASequences {
    public List<String> findRepeatedDnaSequences(String s) {
        Set<String> seen = new HashSet<>(), out = new HashSet<>();
        for (int i = 0; i + 10 <= s.length(); i++) {
            String sub = s.substring(i, i + 10);
            if (!seen.add(sub)) out.add(sub);
        }
        return new ArrayList<>(out);
    }
}
