package com.company.DSA.bst;

import com.company.DSA.common.TreeNode;

/* ============================================================
 *  LeetCode #98 — Validate Binary Search Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  Determine if a binary tree is a valid BST.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [2,1,3]                  → true
 *  Ex2: [5,1,4,null,null,3,6]    → false
 *  Ex3: [10,5,15,null,null,6,20] → false
 *  Ex4: [2,2,2]                  → false  (strict)
 *
 *  CONSTRAINTS:  1 <= nodes <= 10^4;  -2^31 <= val <= 2^31 - 1.
 *
 *  HINTS
 *  -----
 *   1. Carry (lo, hi) BOUNDS down the recursion.
 *   2. Use Long bounds to handle Integer.MIN/MAX values.
 * ============================================================ */
public class ValidateBST {

    public boolean isValidBST(final TreeNode root) {
        return inBounds(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean inBounds(final TreeNode node,
                             final long lowerBoundExclusive,
                             final long upperBoundExclusive) {
        if (node == null) {
            return true;
        }
        if (node.val <= lowerBoundExclusive || node.val >= upperBoundExclusive) {
            return false;
        }
        return inBounds(node.left,  lowerBoundExclusive, node.val)
            && inBounds(node.right, node.val,            upperBoundExclusive);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Pass an open interval (lo, hi). Going left tightens upper bound; going
 *  right tightens lower bound.
 *
 *  Complexity: Time O(n), Space O(h) recursion.
 *  Pattern: "pass invariants down the recursion."
 * ============================================================ */
