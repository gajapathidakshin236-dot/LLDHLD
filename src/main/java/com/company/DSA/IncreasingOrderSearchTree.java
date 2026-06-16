package com.company.DSA;

/**
 * LeetCode #897 - Increasing Order Search Tree
 * Inorder traversal; rebuild as right-only chain using a tail pointer.
 * Time: O(n)  Space: O(h)
 */
public class IncreasingOrderSearchTree {
    TreeNode tail;
    public TreeNode increasingBST(TreeNode root) {
        TreeNode dummy = new TreeNode(0);
        tail = dummy;
        inorder(root);
        return dummy.right;
    }
    private void inorder(TreeNode n) {
        if (n == null) return;
        inorder(n.left);
        n.left = null; tail.right = n; tail = n;
        inorder(n.right);
    }
}
