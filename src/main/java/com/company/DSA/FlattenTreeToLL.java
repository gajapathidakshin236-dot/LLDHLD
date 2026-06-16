package com.company.DSA;

/**
 * LeetCode #114 - Flatten Binary Tree to Linked List (preorder, in-place)
 * Morris-like: if left exists, find rightmost of left subtree, attach current right after it.
 * Time: O(n)  Space: O(1)
 */
public class FlattenTreeToLL {
    public void flatten(TreeNode root) {
        TreeNode cur = root;
        while (cur != null) {
            if (cur.left != null) {
                TreeNode r = cur.left;
                while (r.right != null) r = r.right;
                r.right = cur.right;
                cur.right = cur.left;
                cur.left = null;
            }
            cur = cur.right;
        }
    }
}
