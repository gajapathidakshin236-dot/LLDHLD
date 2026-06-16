package com.company.DSA;

/* ============================================================
 *  LeetCode #114 — Flatten Binary Tree to Linked List
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given the root of a binary tree, flatten it to a "linked list" in place:
 *  - All nodes appear right-linked (left = null).
 *  - Order follows PREORDER traversal.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,5,3,4,null,6] → [1,null,2,null,3,null,4,null,5,null,6]
 *  Ex2: [] → []
 *  Ex3: [0] → [0]
 *
 *  CONSTRAINTS:  0 <= n <= 2000;  -100 <= val <= 100.
 *
 *  HINTS
 *  -----
 *   1. Morris-like: for each node, if left exists, find rightmost of left subtree,
 *      attach current right after it, then move left to right and null out left.
 *   2. Repeat with cur = cur.right.
 * ============================================================ */
public class FlattenTreeToLL {
    public void flatten(TreeNode root) {
        TreeNode cur = root;
        while (cur != null) {
            if (cur.left != null) {
                TreeNode r = cur.left;
                while (r.right != null) r = r.right;
                r.right = cur.right;
                cur.right = cur.left;
                cur.left = null;
            }
            cur = cur.right;
        }
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  In-place reordering using a Morris-like trick:
 *  For each node with a left subtree, the predecessor in preorder is the
 *  rightmost node of the left subtree. Attach the current right subtree
 *  after this predecessor, then move the entire left subtree to the right
 *  and clear left.
 *
 *  Why O(n):
 *    Each edge is visited a constant number of times across the whole walk.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Edge cases: empty; left or right skewed.
 *  Pattern: Morris-style in-place restructuring.
 * ============================================================ */
