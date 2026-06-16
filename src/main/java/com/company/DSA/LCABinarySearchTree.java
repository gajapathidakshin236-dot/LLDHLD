package com.company.DSA;

/**
 * LeetCode #235 - LCA of a BST
 * Walk down: if both p,q smaller go left; if both larger go right; else current.
 * Time: O(h)  Space: O(1)
 */
public class LCABinarySearchTree {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        while (root != null) {
            if (p.val < root.val && q.val < root.val) root = root.left;
            else if (p.val > root.val && q.val > root.val) root = root.right;
            else return root;
        }
        return null;
    }
}
