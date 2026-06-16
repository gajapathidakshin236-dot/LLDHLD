package com.company.DSA;

public class BST {
    public static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    public TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null) return new TreeNode(val);
        if (val < root.val) root.left  = insertIntoBST(root.left,  val);
        else                root.right = insertIntoBST(root.right, val);
        return root;
    }

/*
*   1                    1
   / \                  / \
  2   2     ✅          2   2     ❌
 / \ / \                \   \
3  4 4  3                3   3*/

    public boolean isSymmetric(TreeNode root) {
        if (root == null) return true;
        return isMirror(root.left, root.right);
    }

    private boolean isMirror(TreeNode a, TreeNode b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.val == b.val
                && isMirror(a.left,  b.right)
                && isMirror(a.right, b.left);
    }


}
