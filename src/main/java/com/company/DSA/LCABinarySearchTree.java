package com.company.DSA;

/* ============================================================
 *  LeetCode #235 — LCA of a Binary SEARCH Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  In a BST, return the LCA of two given nodes p and q.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: root=[6,2,8,0,4,7,9,null,null,3,5], p=2, q=8 → 6
 *  Ex2: root above, p=2, q=4 → 2  (a node is its own ancestor)
 *  Ex3: root=[2,1], p=2, q=1 → 2
 *
 *  CONSTRAINTS:  2 <= nodes <= 10^5; all values unique; p, q exist.
 *
 *  HINTS
 *  -----
 *   1. Use the BST ordering, not generic tree DFS.
 *   2. If both p and q are SMALLER than current → go left.
 *   3. If both LARGER → go right. Else current is split point = LCA.
 * ============================================================ */
public class LCABinarySearchTree {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        while (root != null) {
            if (p.val < root.val && q.val < root.val) root = root.left;
            else if (p.val > root.val && q.val > root.val) root = root.right;
            else return root;
        }
        return null;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Use the BST property: the LCA is the FIRST node where p and q diverge —
 *  one going left and the other right. So walk down comparing values:
 *    both < current → both lie in left subtree → go left.
 *    both > current → both lie in right subtree → go right.
 *    otherwise → split here → this IS the LCA.
 *
 *  Complexity: Time O(h), Space O(1) iterative or O(h) recursive.
 *  Edge cases: p or q equals root → return root immediately.
 *  Pattern: leverage BST structure for log-time tree queries.
 * ============================================================ */
