package com.company.DSA.bst;

import com.company.DSA.common.TreeNode;

/* ============================================================
 *  LeetCode #99 — Recover Binary Search Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  Two nodes were swapped by mistake. Recover by swapping their values back.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,3,null,null,2] → [3,1,null,null,2]
 *  Ex2: [3,1,4,null,null,2] → [2,1,4,null,null,3]
 *
 *  CONSTRAINTS:  2 <= n <= 1000;  -2^31 <= val <= 2^31 - 1.
 *
 *  HINTS
 *  -----
 *   1. Inorder yields strictly ascending in valid BST.
 *   2. Adjacent swap → ONE violation; non-adjacent → TWO violations.
 * ============================================================ */
public class RecoverBST {

    private TreeNode lastInorderNode;
    private TreeNode firstMisplaced;
    private TreeNode secondMisplaced;

    public void recoverTree(final TreeNode root) {
        inorder(root);

        final int temp        = firstMisplaced.val;
        firstMisplaced.val    = secondMisplaced.val;
        secondMisplaced.val   = temp;
    }

    private void inorder(final TreeNode node) {
        if (node == null) {
            return;
        }
        inorder(node.left);

        if (lastInorderNode != null && lastInorderNode.val > node.val) {
            if (firstMisplaced == null) {
                firstMisplaced = lastInorderNode;
            }
            secondMisplaced = node;
        }
        lastInorderNode = node;

        inorder(node.right);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Inorder visits BST values in sorted order in a valid BST. A swap creates
 *  one or two inversions. First inversion's `lastInorderNode` is one swapped
 *  node; second inversion's `node` is the other. Adjacent swap is a single
 *  inversion → both nodes captured in one go.
 *
 *  Complexity: Time O(n), Space O(h) recursion.
 *  Pattern: anomaly detection in sorted traversal.
 * ============================================================ */
