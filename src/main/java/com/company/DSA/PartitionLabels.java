package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #763 — Partition Labels
 * ============================================================
 *  PROBLEM
 *  -------
 *  Partition string s into AS MANY parts as possible so that each letter
 *  appears in at most one part. Return the sizes of those parts.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: "ababcbacadefegdehijhklij" → [9,7,8]
 *      Parts: "ababcbaca", "defegde", "hijhklij"
 *  Ex2: "eccbbbbdec" → [10]   (all letters intertwine)
 *  Ex3: "a" → [1]
 *
 *  CONSTRAINTS:  1 <= s.length <= 500; lowercase letters.
 *
 *  HINTS
 *  -----
 *   1. Record the LAST index of every character.
 *   2. Greedy sweep: maintain partition end = max(end, lastIndex(c)) as you walk.
 *   3. When i == end, close the partition.
 * ============================================================ */
public class PartitionLabels {
    public List<Integer> partitionLabels(String s) {
        int[] last = new int[26];
        for (int i = 0; i < s.length(); i++) last[s.charAt(i) - 'a'] = i;
        List<Integer> res = new ArrayList<>();
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            end = Math.max(end, last[s.charAt(i) - 'a']);
            if (i == end) { res.add(end - start + 1); start = i + 1; }
        }
        return res;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Each character forces the partition to extend at least to its LAST occurrence.
 *  Sweep left to right; expand `end` to last[c] for each c. When i finally
 *  catches up to `end`, no later index needs anything from the current window —
 *  safe to cut.
 *
 *  Why greedy works:
 *    The "partition end" is constrained from below; cutting earlier would put
 *    a character into two partitions. Cutting exactly at the constraint is
 *    optimal (most parts).
 *
 *  Complexity: Time O(n), Space O(1) — bounded by 26.
 *  Edge cases: all same letter → one big partition; all distinct → all 1-sized.
 *  Pattern: "sweep + extend constraint" greedy. Same flavor as #45 Jump Game II.
 * ============================================================ */
