package com.company.DSA;

/* ============================================================
 *  LeetCode #99 — Recover Binary Search Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  Two nodes of a BST were swapped by mistake. Recover the tree WITHOUT
 *  changing its structure.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,3,null,null,2] → [3,1,null,null,2]
 *  Ex2: [3,1,4,null,null,2] → [2,1,4,null,null,3]
 *
 *  CONSTRAINTS:  2 <= n <= 1000;  -2^31 <= val <= 2^31 - 1.
 *
 *  HINTS
 *  -----
 *   1. Inorder traversal of a VALID BST is strictly ascending. Find the violations.
 *   2. Adjacent swap → ONE violation; non-adjacent swap → TWO violations.
 *   3. First violation: prev. Second violation (or same if only one): cur. Swap their values.
 * ============================================================ */
public class RecoverBST {
    TreeNode prev, first, second;
    public void recoverTree(TreeNode root) {
        inorder(root);
        int t = first.val; first.val = second.val; second.val = t;
    }
    private void inorder(TreeNode n) {
        if (n == null) return;
        inorder(n.left);
        if (prev != null && prev.val > n.val) {
            if (first == null) first = prev;
            second = n;
        }
        prev = n;
        inorder(n.right);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Inorder visits nodes in sorted order in a valid BST. A single swap creates
 *  one or two "inversions" in the sorted sequence:
 *    Case 1 — non-adjacent swap → two inversions; first = first-prev of inv 1,
 *             second = cur of inv 2.
 *    Case 2 — adjacent swap → one inversion; first = prev, second = cur.
 *  Inorder traverse picks them up; swap values at the end.
 *
 *  Complexity: Time O(n), Space O(h) recursion or O(1) Morris.
 *  Pattern: anomaly detection in sorted traversal.
 * ============================================================ */
