package com.company.DSA.bst;

import com.company.DSA.common.TreeNode;

/* ============================================================
 *  LeetCode #669 — Trim a Binary Search Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  Trim so all values lie in [lo, hi]. Preserve relative structure.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: root=[1,0,2], lo=1, hi=2 → [1,null,2]
 *  Ex2: root=[3,0,4,null,2,null,null,1], lo=1, hi=3 → [3,2,null,1]
 *
 *  CONSTRAINTS:  1 <= n <= 10^4;  0 <= val <= 10^4;  unique.
 *
 *  HINTS
 *  -----
 *   1. If root.val < lo → trim returns the trimmed RIGHT subtree.
 *   2. If root.val > hi → trim returns the trimmed LEFT subtree.
 * ============================================================ */
public class TrimBST {

    public TreeNode trimBST(final TreeNode root, final int lowerBound, final int upperBound) {
        if (root == null) {
            return null;
        }
        if (root.val < lowerBound) {
            return trimBST(root.right, lowerBound, upperBound);
        }
        if (root.val > upperBound) {
            return trimBST(root.left,  lowerBound, upperBound);
        }
        root.left  = trimBST(root.left,  lowerBound, upperBound);
        root.right = trimBST(root.right, lowerBound, upperBound);
        return root;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  BST-aware DFS. If current value is below lo, entire LEFT subtree is also
 *  below lo (BST property) → discard, return trimmed RIGHT. Symmetric for > hi.
 *
 *  Complexity: Time O(n), Space O(h).
 *  Pattern: BST DFS with branch pruning.
 * ============================================================ */
