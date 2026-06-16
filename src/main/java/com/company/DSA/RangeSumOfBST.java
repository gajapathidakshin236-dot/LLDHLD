package com.company.DSA;

/**
 * LeetCode #938 - Range Sum of BST
 * DFS pruning: if val<lo go right; if val>hi go left; else add and recurse both.
 * Time: O(n)  Space: O(h)
 */
public class RangeSumOfBST {
    public int rangeSumBST(TreeNode root, int lo, int hi) {
        if (root == null) return 0;
        if (root.val < lo) return rangeSumBST(root.right, lo, hi);
        if (root.val > hi) return rangeSumBST(root.left, lo, hi);
        return root.val + rangeSumBST(root.left, lo, hi) + rangeSumBST(root.right, lo, hi);
    }
}
