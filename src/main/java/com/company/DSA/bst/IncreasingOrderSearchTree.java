package com.company.DSA.bst;

import com.company.DSA.common.TreeNode;

/* ============================================================
 *  LeetCode #897 — Increasing Order Search Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  Reorder a BST into a right-skewed chain (left always null), values ascending.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [5,3,6,2,4,null,8,1,null,null,null,7,9] → chain 1→2→3→4→5→6→7→8→9
 *  Ex2: [5,1,7] → 1→5→7
 *
 *  CONSTRAINTS:  1 <= n <= 100; 0 <= val <= 1000.
 *
 *  HINTS
 *  -----
 *   1. Inorder traversal gives ascending order in BST.
 *   2. Append each visited node to a tail; null out lefts.
 * ============================================================ */
public class IncreasingOrderSearchTree {

    private TreeNode chainTail;

    public TreeNode increasingBST(final TreeNode root) {
        final TreeNode headSentinel = new TreeNode(0);
        chainTail = headSentinel;
        inorderRebuild(root);
        return headSentinel.right;
    }

    private void inorderRebuild(final TreeNode node) {
        if (node == null) {
            return;
        }
        inorderRebuild(node.left);

        node.left       = null;
        chainTail.right = node;
        chainTail       = node;

        inorderRebuild(node.right);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Inorder traversal visits nodes ascending. Append each to a running tail
 *  pointer and null out its left.
 *
 *  Complexity: Time O(n), Space O(h) recursion.
 *  Pattern: traversal-as-rewrite.
 * ============================================================ */
