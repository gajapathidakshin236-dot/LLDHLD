package com.company.DSA;

/**
 * LeetCode #783 - Minimum Distance Between BST Nodes
 * Inorder gives sorted order; min diff is between consecutive pairs.
 * Time: O(n)  Space: O(h)
 */
public class MinDistanceBSTNodes {
    Integer prev = null;
    int best = Integer.MAX_VALUE;
    public int minDiffInBST(TreeNode root) {
        inorder(root);
        return best;
    }
    private void inorder(TreeNode n) {
        if (n == null) return;
        inorder(n.left);
        if (prev != null) best = Math.min(best, n.val - prev);
        prev = n.val;
        inorder(n.right);
    }
}
