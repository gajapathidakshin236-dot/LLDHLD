package com.company.DSA.bst;

import com.company.DSA.common.TreeNode;

/* ============================================================
 *  LeetCode #938 — Range Sum of BST
 * ============================================================
 *  PROBLEM
 *  -------
 *  Sum of all node values in [lo, hi].
 *
 *  EXAMPLES
 *  --------
 *  Ex1: root=[10,5,15,3,7,null,18], lo=7, hi=15 → 32
 *
 *  CONSTRAINTS:  1 <= n <= 2*10^4;  unique; 1 <= val/lo/hi <= 10^5.
 *
 *  HINTS
 *  -----
 *   1. PRUNE subtrees that fall outside [lo, hi] using BST property.
 * ============================================================ */
public class RangeSumOfBST {

    public int rangeSumBST(final TreeNode node, final int lowerBound, final int upperBound) {
        if (node == null) {
            return 0;
        }
        if (node.val < lowerBound) {
            return rangeSumBST(node.right, lowerBound, upperBound);
        }
        if (node.val > upperBound) {
            return rangeSumBST(node.left,  lowerBound, upperBound);
        }
        return node.val
             + rangeSumBST(node.left,  lowerBound, upperBound)
             + rangeSumBST(node.right, lowerBound, upperBound);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  DFS with pruning: if val < lo, only right subtree relevant; if val > hi,
 *  only left subtree relevant; else add and recurse both.
 *
 *  Complexity: Time O(n) worst, O(h + k) average. Space O(h).
 *  Pattern: BST-aware traversal.
 * ============================================================ */
