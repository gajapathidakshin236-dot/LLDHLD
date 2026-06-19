package com.company.DSA.bst;

import com.company.DSA.common.TreeNode;

/* ============================================================
 *  LeetCode #235 — LCA of a Binary SEARCH Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  In a BST, return the LCA of two given nodes p and q.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: root=[6,2,8,...], p=2, q=8 → 6
 *  Ex2: p=2, q=4 → 2  (a node is its own ancestor)
 *
 *  CONSTRAINTS:  2 <= nodes <= 10^5; values unique; p, q exist.
 *
 *  HINTS
 *  -----
 *   1. If both p and q are SMALLER than current → go left.
 *   2. If both LARGER → go right. Else split point = LCA.
 * ============================================================ */
public class LCABinarySearchTree {

    public TreeNode lowestCommonAncestor(TreeNode current,
                                         final TreeNode firstTarget,
                                         final TreeNode secondTarget) {
        while (current != null) {
            if (firstTarget.val < current.val && secondTarget.val < current.val) {
                current = current.left;
            } else if (firstTarget.val > current.val && secondTarget.val > current.val) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Use BST ordering. The LCA is the FIRST node where p and q diverge — one
 *  going left and the other right. Walk down comparing values.
 *
 *  Complexity: Time O(h), Space O(1).
 *  Pattern: leverage BST structure for log-time tree queries.
 * ============================================================ */
