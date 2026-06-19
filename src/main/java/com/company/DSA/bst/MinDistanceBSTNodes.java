package com.company.DSA.bst;

import com.company.DSA.common.TreeNode;

/* ============================================================
 *  LeetCode #783 — Minimum Distance Between BST Nodes
 * ============================================================
 *  PROBLEM
 *  -------
 *  Min difference between any two nodes' values in BST.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [4,2,6,1,3] → 1
 *  Ex2: [1,0,48,null,null,12,49] → 1
 *
 *  CONSTRAINTS:  2 <= nodes <= 100;  0 <= val <= 10^5;  unique.
 *
 *  HINTS
 *  -----
 *   1. Inorder of BST is sorted. Min diff = min consecutive diff.
 * ============================================================ */
public class MinDistanceBSTNodes {

    private Integer previousInorderValue;
    private int minDiffSoFar = Integer.MAX_VALUE;

    public int minDiffInBST(final TreeNode root) {
        inorder(root);
        return minDiffSoFar;
    }

    private void inorder(final TreeNode node) {
        if (node == null) {
            return;
        }
        inorder(node.left);

        if (previousInorderValue != null) {
            minDiffSoFar = Math.min(minDiffSoFar, node.val - previousInorderValue);
        }
        previousInorderValue = node.val;

        inorder(node.right);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Inorder traversal emits BST values ascending. Closest pair in a sorted
 *  sequence is always two consecutive elements.
 *
 *  Complexity: Time O(n), Space O(h).
 *  Pattern: sorted-stream pairwise minimum.
 * ============================================================ */
