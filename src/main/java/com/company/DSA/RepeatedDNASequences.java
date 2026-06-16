package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #187 — Repeated DNA Sequences
 * ============================================================
 *  PROBLEM
 *  -------
 *  Find all 10-letter substrings of s that appear MORE THAN ONCE.
 *  s contains only 'A','C','G','T'.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT" → ["AAAAACCCCC","CCCCCAAAAA"]
 *  Ex2: "AAAAAAAAAAAAA"                    → ["AAAAAAAAAA"]
 *  Ex3: "ACGT"                              → []
 *
 *  CONSTRAINTS:  1 <= s.length <= 10^5; alphabet {A,C,G,T}.
 *
 *  HINTS
 *  -----
 *   1. Sliding window of 10 chars. Use a HashSet "seen" and a HashSet "result".
 *   2. If add() to "seen" returns false → duplicate → add to result.
 *   3. Rolling hash optimization possible but rarely needed for n <= 10^5.
 * ============================================================ */
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

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Slide a window of length 10; each candidate substring is hashed via the
 *  default String hashing. HashSet.add returns false if already present →
 *  that's our duplicate signal.
 *
 *  Why "out" is also a Set:
 *    A 10-mer could appear 3+ times; we want it only ONCE in output.
 *
 *  Complexity: Time O(n * 10) substring + hashing, Space O(n).
 *  Edge cases: length < 10 → empty; entire string is one 10-mer → no dups.
 *  Pattern: fixed-window dedup. Rolling hash gives O(n) if we want to scale.
 * ============================================================ */
