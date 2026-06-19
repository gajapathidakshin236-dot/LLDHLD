package com.company.DSA.binarytree;

import com.company.DSA.common.TreeNode;

/* ============================================================
 *  LeetCode #236 — Lowest Common Ancestor of a Binary Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given root and two distinct nodes p, q (both present), return their LCA.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: root=[3,5,1,...], p=5, q=1 → 3
 *  Ex2: p=5, q=4 → 5  (a node is its own ancestor)
 *  Ex3: root=[1,2], p=1, q=2 → 1
 *
 *  CONSTRAINTS:  2 <= nodes <= 10^5; values unique; p, q exist in tree.
 *
 *  HINTS
 *  -----
 *   1. Recurse left and right. If both come back non-null → current is LCA.
 *   2. If exactly one is non-null → propagate it up.
 * ============================================================ */
public class LowestCommonAncestor {

    public TreeNode lowestCommonAncestor(final TreeNode root,
                                         final TreeNode firstTarget,
                                         final TreeNode secondTarget) {
        if (root == null || root == firstTarget || root == secondTarget) {
            return root;
        }

        final TreeNode foundInLeftSubtree  = lowestCommonAncestor(root.left,  firstTarget, secondTarget);
        final TreeNode foundInRightSubtree = lowestCommonAncestor(root.right, firstTarget, secondTarget);

        if (foundInLeftSubtree != null && foundInRightSubtree != null) {
            return root;
        }
        return (foundInLeftSubtree != null) ? foundInLeftSubtree : foundInRightSubtree;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Post-order recursion. Each call returns:
 *    - p or q if found in this subtree,
 *    - the LCA if both have been found below,
 *    - null otherwise.
 *  If both left and right return non-null, current root is the LCA.
 *
 *  Complexity: Time O(n), Space O(h) recursion.
 *  Pattern: post-order recursion returning informative values.
 * ============================================================ */
