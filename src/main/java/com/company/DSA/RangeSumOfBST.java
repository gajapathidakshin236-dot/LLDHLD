package com.company.DSA;

/* ============================================================
 *  LeetCode #938 — Range Sum of BST
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given the root of a BST and inclusive range [lo, hi], return the sum of
 *  all node values that fall in that range.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: root=[10,5,15,3,7,null,18], lo=7, hi=15 → 32  (7+10+15)
 *  Ex2: root=[10,5,15,3,7,13,18,1,null,6], lo=6, hi=10 → 23
 *
 *  CONSTRAINTS:  1 <= n <= 2*10^4;  unique values; 1 <= val/lo/hi <= 10^5.
 *
 *  HINTS
 *  -----
 *   1. Use BST order to PRUNE subtrees that fall outside [lo, hi].
 *   2. If val < lo → only right subtree may contain >= lo values.
 *   3. If val > hi → only left subtree.
 *   4. Otherwise add val and recurse both.
 * ============================================================ */
public class RangeSumOfBST {
    public int rangeSumBST(TreeNode root, int lo, int hi) {
        if (root == null) return 0;
        if (root.val < lo) return rangeSumBST(root.right, lo, hi);
        if (root.val > hi) return rangeSumBST(root.left, lo, hi);
        return root.val + rangeSumBST(root.left, lo, hi) + rangeSumBST(root.right, lo, hi);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  DFS with pruning. Without pruning, we'd visit every node O(n). With BST
 *  pruning we avoid entire subtrees that can't be in range.
 *
 *  Complexity: Time O(n) worst, O(h + k) average where k = nodes in range.
 *  Space: O(h) recursion.
 *  Edge cases: empty subtree; entire range covers tree; range outside all values.
 *  Pattern: BST-aware traversal — same idea: #270 closest, #783 min diff.
 * ============================================================ */
