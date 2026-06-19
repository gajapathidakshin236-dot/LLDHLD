package com.company.DSA.common;

/* ============================================================
 *  Shared TreeNode helper for binary tree problems.
 *  Used across binarytree/ and bst/ subpackages.
 * ============================================================ */
public class TreeNode {
    public int val;
    public TreeNode left, right;
    public TreeNode() {}
    public TreeNode(int v) { this.val = v; }
    public TreeNode(int v, TreeNode l, TreeNode r) { this.val = v; this.left = l; this.right = r; }
}
