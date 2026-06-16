package com.company.DSA;

/**
 * LeetCode #99 - Recover Binary Search Tree
 * Inorder traversal; two violations of strictly increasing.
 *   first violation: prev (then maybe more). last violation: cur.
 * Swap their values. Morris O(1) or recursion O(h) stack.
 * Time: O(n)  Space: O(h)
 */
public class RecoverBST {
    TreeNode prev, first, second;
    public void recoverTree(TreeNode root) {
        inorder(root);
        int t = first.val; first.val = second.val; second.val = t;
    }
    private void inorder(TreeNode n) {
        if (n == null) return;
        inorder(n.left);
        if (prev != null && prev.val > n.val) {
            if (first == null) first = prev;
            second = n;
        }
        prev = n;
        inorder(n.right);
    }
}
