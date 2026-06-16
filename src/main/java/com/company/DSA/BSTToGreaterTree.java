package com.company.DSA;

/**
 * LeetCode #538 - Convert BST to Greater Tree
 * Reverse inorder (R, root, L) carrying running sum.
 * Time: O(n)  Space: O(h)
 */
public class BSTToGreaterTree {
    int sum = 0;
    public TreeNode convertBST(TreeNode root) {
        if (root != null) {
            convertBST(root.right);
            sum += root.val;
            root.val = sum;
            convertBST(root.left);
        }
        return root;
    }
}
