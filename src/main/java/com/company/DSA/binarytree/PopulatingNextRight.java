package com.company.DSA.binarytree;

/* ============================================================
 *  LeetCode #116 — Populating Next Right Pointers in Each Node
 * ============================================================
 *  PROBLEM
 *  -------
 *  Perfect binary tree: populate each node's `next` to point to the next
 *  right node on its level (or null).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,3,4,5,6,7]
 *       1 → null
 *       2 → 3 → null
 *       4 → 5 → 6 → 7 → null
 *
 *  CONSTRAINTS:  perfect BT; depth <= 10; 0 <= count <= 2^11 - 1.
 *
 *  HINTS
 *  -----
 *   1. Use already-linked `next` on level above as a "linked list".
 *   2. For each node head on a level, connect its two children + cross gaps via head.next.
 * ============================================================ */
public class PopulatingNextRight {

    public static class Node {
        public int val;
        public Node left, right, next;
        public Node() {}
        public Node(final int v) { this.val = v; }
    }

    public Node connect(final Node root) {
        Node leftmostOnLevel = root;

        while (leftmostOnLevel != null && leftmostOnLevel.left != null) {
            Node parentCursor = leftmostOnLevel;

            while (parentCursor != null) {
                parentCursor.left.next = parentCursor.right;

                if (parentCursor.next != null) {
                    parentCursor.right.next = parentCursor.next.left;
                }
                parentCursor = parentCursor.next;
            }
            leftmostOnLevel = leftmostOnLevel.left;
        }
        return root;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Iterate one level at a time. The parent level's `next` pointers form a
 *  linked list, letting us connect siblings of children + cross-parent
 *  bridges, with no queue needed.
 *
 *  Complexity: Time O(n), Space O(1) (besides output).
 *  Pattern: "reuse what you've built." For non-perfect BT see #117.
 * ============================================================ */
