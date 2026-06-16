package com.company.DSA;

/* ============================================================
 *  LeetCode #236 — Lowest Common Ancestor of a Binary Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given root and two distinct nodes p, q (both present), return their LCA.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: root=[3,5,1,6,2,0,8,null,null,7,4], p=5, q=1 → 3
 *  Ex2: root=[3,5,1,...],                  p=5, q=4 → 5  (an ancestor of itself)
 *  Ex3: root=[1,2],                        p=1, q=2 → 1
 *
 *  CONSTRAINTS:  2 <= nodes <= 10^5; values unique; p, q exist in tree.
 *
 *  HINTS
 *  -----
 *   1. Recurse left and right. If both come back non-null → current is LCA.
 *   2. If exactly one is non-null → propagate it up.
 *   3. A node is the LCA of itself and its descendant.
 * ============================================================ */
public class LowestCommonAncestor {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;
        TreeNode L = lowestCommonAncestor(root.left, p, q);
        TreeNode R = lowestCommonAncestor(root.right, p, q);
        if (L != null && R != null) return root;
        return L != null ? L : R;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Post-order recursion. Each call returns one of:
 *    - p, q, or LCA if found in this subtree.
 *    - null otherwise.
 *
 *  If both L and R are non-null, p was found in one subtree and q in the other,
 *  so current root is the LCA. Otherwise, propagate the non-null upward.
 *
 *  Why returning p or q itself works:
 *    A node IS its own ancestor. So if root == p, we return root immediately.
 *    The other call will eventually find q below and propagate. At their LCA,
 *    both subtree calls return non-null → root is LCA.
 *
 *  Complexity: Time O(n) one visit per node, Space O(h) recursion.
 *  Edge cases: root is p or q → return root.
 *  Pattern: post-order recursion returning informative values.
 * ============================================================ */
