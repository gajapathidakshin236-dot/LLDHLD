package com.company.DSA;

import java.util.*;

/**
 * LeetCode #2007 - Find Original Array From Doubled Array
 * Sort, walk via deque per-value queue. For each x, must consume one 2x.
 * Time: O(n log n)  Space: O(n)
 */
public class OriginalFromDoubled {
    public int[] findOriginalArray(int[] changed) {
        if (changed.length % 2 != 0) return new int[0];
        Map<Integer, Integer> cnt = new HashMap<>();
        for (int x : changed) cnt.merge(x, 1, Integer::sum);
        int[] sorted = changed.clone();
        Arrays.sort(sorted);
        int[] res = new int[changed.length / 2];
        int k = 0;
        for (int x : sorted) {
            if (cnt.getOrDefault(x, 0) == 0) continue;
            if (cnt.getOrDefault(2 * x, 0) == 0) return new int[0];
            res[k++] = x;
            cnt.merge(x, -1, Integer::sum);
            cnt.merge(2 * x, -1, Integer::sum);
        }
        return res;
    }
}
