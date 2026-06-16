package com.company.DSA;

/* ============================================================
 *  Shared TreeNode helper for binary tree problems.
 *  Used by: LevelOrderTraversal, ValidateBST, LCA, Diameter, etc.
 * ============================================================ */
public class TreeNode {
    public int val;
    public TreeNode left, right;
    public TreeNode() {}
    public TreeNode(int v) { this.val = v; }
    public TreeNode(int v, TreeNode l, TreeNode r) { this.val = v; this.left = l; this.right = r; }
}
