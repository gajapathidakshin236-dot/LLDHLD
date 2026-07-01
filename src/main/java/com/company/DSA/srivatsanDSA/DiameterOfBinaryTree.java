package com.company.DSA.srivatsanDSA;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 23
 *  Problem: Diameter of Binary Tree
 *  Longest path between any two nodes (in edges).
 *
 *  APPROACH (from notes):
 *    int maxDiameter = 0;
 *    height(root):
 *       if null → 0;
 *       int left  = height(root.left);
 *       int right = height(root.right);
 *       maxDiameter = max(maxDiameter, left + right);
 *       return 1 + max(left, right);
 *    Bottom-up; left + right at each node is the path THROUGH that node.
 * ============================================================ */
public class DiameterOfBinaryTree {

    private int longestPathSeen = 0;

    public int diameterOfBinaryTree(final TreeNode root) {
        computeHeight(root);
        return longestPathSeen;
    }

    private int computeHeight(final TreeNode node) {
        if (node == null) {
            return 0;
        }
        final int leftHeight  = computeHeight(node.left);
        final int rightHeight = computeHeight(node.right);
        longestPathSeen = Math.max(longestPathSeen, leftHeight + rightHeight);
        return 1 + Math.max(leftHeight, rightHeight);
    }
}
