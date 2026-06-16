package com.company.DSA;

/**
 * LeetCode #98 - Validate BST
 * Recurse with (lo, hi) bounds; every node must be strictly in (lo, hi).
 * Use Long bounds to avoid Integer.MIN/MAX edge cases.
 * Time: O(n)  Space: O(h)
 */
public class ValidateBST {
    public boolean isValidBST(TreeNode root) {
        return dfs(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }
    private boolean dfs(TreeNode n, long lo, long hi) {
        if (n == null) return true;
        if (n.val <= lo || n.val >= hi) return false;
        return dfs(n.left, lo, n.val) && dfs(n.right, n.val, hi);
    }
}
