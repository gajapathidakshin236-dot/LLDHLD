package com.company.DSA.srivatsanDSA;

/** Local TreeNode used by tree problems in srivatsanDSA. */
public class TreeNode {
    public int val;
    public TreeNode left, right;
    public TreeNode() {}
    public TreeNode(final int v) { this.val = v; }
    public TreeNode(final int v, final TreeNode l, final TreeNode r) {
        this.val = v; this.left = l; this.right = r;
    }
}
