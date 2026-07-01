package com.company.DSA.srivatsanDSA;

import java.util.HashMap;
import java.util.Map;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 66
 *  Problem: Copy List with Random Pointer
 *  Each node has next + random pointer. Return deep copy.
 *
 *  APPROACH (from notes) — 2 iterations:
 *    Pass 1: create a clone of each node and store map[old] = new.
 *    Pass 2: walk old list again; clone.next   = map.get(old.next);
 *                                  clone.random = map.get(old.random).
 *    Return map.get(head).
 * ============================================================ */
public class CopyRandomList {

    public static class RandomNode {
        public int val;
        public RandomNode next;
        public RandomNode random;
        public RandomNode(final int v) { this.val = v; }
    }

    public RandomNode copyRandomList(final RandomNode originalHead) {
        if (originalHead == null) {
            return null;
        }
        final Map<RandomNode, RandomNode> cloneByOriginal = new HashMap<>();

        // Pass 1: clone nodes
        for (RandomNode cursor = originalHead; cursor != null; cursor = cursor.next) {
            cloneByOriginal.put(cursor, new RandomNode(cursor.val));
        }

        // Pass 2: stitch next & random
        for (RandomNode cursor = originalHead; cursor != null; cursor = cursor.next) {
            final RandomNode clone = cloneByOriginal.get(cursor);
            clone.next   = cloneByOriginal.get(cursor.next);
            clone.random = cloneByOriginal.get(cursor.random);
        }
        return cloneByOriginal.get(originalHead);
    }
}
