package com.company.DSA;

/* ============================================================
 *  LeetCode #116 — Populating Next Right Pointers in Each Node
 * ============================================================
 *  PROBLEM
 *  -------
 *  Each node in a PERFECT binary tree has a `next` pointer. Populate each
 *  next to point to the next right node on its level (or null if none).
 *  Bonus: O(1) extra space (recursion stack ok).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: root=[1,2,3,4,5,6,7]
 *       1 → null
 *       2 → 3 → null
 *       4 → 5 → 6 → 7 → null
 *
 *  CONSTRAINTS:  perfect BT; depth <= 10; 0 <= node count <= 2^11 -1.
 *
 *  HINTS
 *  -----
 *   1. Use the EXISTING next pointers on level above as a "linked list".
 *   2. For each node head on a level, connect its two children + cross gaps via head.next.
 * ============================================================ */
public class PopulatingNextRight {
    static class Node { int val; Node left, right, next; Node(int v) { val = v; } }
    public Node connect(Node root) {
        Node leftmost = root;
        while (leftmost != null && leftmost.left != null) {
            Node head = leftmost;
            while (head != null) {
                head.left.next = head.right;
                if (head.next != null) head.right.next = head.next.left;
                head = head.next;
            }
            leftmost = leftmost.left;
        }
        return root;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Iterate one level at a time. For each node on the current level, connect:
 *    - left.next = right (its own children).
 *    - right.next = head.next.left (cross-parent connection) when head.next exists.
 *  Then drop down via leftmost.left.
 *  No queue needed — the `next` pointers on the upper level act like a linked list.
 *
 *  Complexity: Time O(n), Space O(1) (besides output).
 *  Edge cases: empty tree; only root → returns root unchanged.
 *  Pattern: "reuse what you've built." For non-perfect BT see #117.
 * ============================================================ */
