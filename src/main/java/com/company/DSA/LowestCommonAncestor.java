package com.company.DSA;

/**
 * LeetCode #236 - LCA of Binary Tree
 * Recurse left/right; if both return non-null, current is LCA. Else propagate the non-null.
 * Time: O(n)  Space: O(h) recursion stack
 */
public class LowestCommonAncestor {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;
        TreeNode L = lowestCommonAncestor(root.left, p, q);
        TreeNode R = lowestCommonAncestor(root.right, p, q);
        if (L != null && R != null) return root;
        return L != null ? L : R;
    }
}
