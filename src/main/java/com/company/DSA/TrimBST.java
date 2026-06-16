package com.company.DSA;

/**
 * LeetCode #669 - Trim a BST
 * If val<lo trim returns right subtree trimmed; if val>hi returns left trimmed; else recurse both.
 * Time: O(n)  Space: O(h)
 */
public class TrimBST {
    public TreeNode trimBST(TreeNode root, int lo, int hi) {
        if (root == null) return null;
        if (root.val < lo) return trimBST(root.right, lo, hi);
        if (root.val > hi) return trimBST(root.left, lo, hi);
        root.left  = trimBST(root.left,  lo, hi);
        root.right = trimBST(root.right, lo, hi);
        return root;
    }
}
