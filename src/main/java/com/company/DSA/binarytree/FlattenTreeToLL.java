package com.company.DSA.binarytree;

import com.company.DSA.common.TreeNode;

/* ============================================================
 *  LeetCode #114 — Flatten Binary Tree to Linked List
 * ============================================================
 *  PROBLEM
 *  -------
 *  Flatten the tree in place so every node has null left and a right-linked
 *  chain following preorder.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,5,3,4,null,6] → 1->2->3->4->5->6 (via .right only)
 *  Ex2: [] → []
 *  Ex3: [0] → [0]
 *
 *  CONSTRAINTS:  0 <= n <= 2000;  -100 <= val <= 100.
 *
 *  HINTS
 *  -----
 *   1. Morris-like trick.
 *   2. For each node, if it has a left subtree, find its rightmost; attach
 *      current right after that rightmost; move left subtree to right.
 * ============================================================ */
public class FlattenTreeToLL {

    public void flatten(final TreeNode root) {
        TreeNode current = root;

        while (current != null) {
            if (current.left != null) {
                final TreeNode rightmostInLeftSubtree = findRightmost(current.left);

                rightmostInLeftSubtree.right = current.right;
                current.right                = current.left;
                current.left                 = null;
            }
            current = current.right;
        }
    }

    private TreeNode findRightmost(TreeNode node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Morris-style in-place reordering. The preorder predecessor of `current` is
 *  the rightmost node in its left subtree. Attach current's right subtree
 *  after that predecessor, then move the entire left subtree to the right.
 *
 *  Complexity: Time O(n), Space O(1).
 *  Pattern: Morris-style restructuring.
 * ============================================================ */
