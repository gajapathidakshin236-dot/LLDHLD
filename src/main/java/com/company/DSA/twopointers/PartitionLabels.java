package com.company.DSA.twopointers;

import java.util.*;

/* ============================================================
 *  LeetCode #763 — Partition Labels
 * ============================================================
 *  PROBLEM
 *  -------
 *  Partition s so each letter appears in at most one part. Return sizes.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "ababcbacadefegdehijhklij" → [9,7,8]
 *  Ex2: "eccbbbbdec" → [10]
 *  Ex3: "a" → [1]
 *
 *  CONSTRAINTS:  1 <= s.length <= 500.
 *
 *  HINTS
 *  -----
 *   1. Record LAST index of each char.
 *   2. Greedy sweep: extend end-pointer; close partition when i == end.
 * ============================================================ */
public class PartitionLabels {

    private static final int ALPHABET_SIZE = 26;

    public List<Integer> partitionLabels(final String input) {
        final int[] lastIndexOfChar = new int[ALPHABET_SIZE];
        for (int index = 0; index < input.length(); index++) {
            lastIndexOfChar[input.charAt(index) - 'a'] = index;
        }

        final List<Integer> partitionSizes = new ArrayList<>();
        int partitionStart = 0;
        int partitionEnd   = 0;

        for (int index = 0; index < input.length(); index++) {
            partitionEnd = Math.max(partitionEnd, lastIndexOfChar[input.charAt(index) - 'a']);

            if (index == partitionEnd) {
                partitionSizes.add(partitionEnd - partitionStart + 1);
                partitionStart = index + 1;
            }
        }
        return partitionSizes;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Each char forces partition to extend at least to its last occurrence.
 *  Sweep left to right; extend `end`; close when index catches up.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: "sweep + extend constraint" greedy.
 * ============================================================ */
