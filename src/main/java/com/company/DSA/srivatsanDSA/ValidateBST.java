package com.company.DSA.srivatsanDSA;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 19-22
 *  Problem: Validate Binary Search Tree
 *  Return true if tree is a valid BST.
 *
 *  APPROACH (from notes):
 *    Recurse with (lo, hi) bounds.
 *      isValid(root, MIN, MAX)
 *      isValid(node, lo, hi):
 *         if node == null → true
 *         if node.val <= lo || node.val >= hi → false
 *         return isValid(left, lo, node.val) && isValid(right, node.val, hi)
 *    Use Long bounds because node values may be Integer.MIN/MAX.
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
