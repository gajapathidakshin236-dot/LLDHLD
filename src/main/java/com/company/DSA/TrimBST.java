package com.company.DSA;

/* ============================================================
 *  LeetCode #669 — Trim a Binary Search Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given a BST and range [lo, hi], trim it so all values lie in [lo, hi].
 *  Preserve relative structure of remaining nodes.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: root=[1,0,2], lo=1, hi=2 → [1,null,2]
 *  Ex2: root=[3,0,4,null,2,null,null,1], lo=1, hi=3 → [3,2,null,1]
 *
 *  CONSTRAINTS:  1 <= n <= 10^4;  0 <= val <= 10^4;  unique values.
 *
 *  HINTS
 *  -----
 *   1. Recurse. If root.val < lo → trim returns the trimmed RIGHT subtree.
 *   2. If root.val > hi → trim returns the trimmed LEFT subtree.
 *   3. Else recurse both, attach, return root.
 * ============================================================ */
public class TrimBST {
    public TreeNode trimBST(TreeNode root, int lo, int hi) {
        if (root == null) return null;
        if (root.val < lo) return trimBST(root.right, lo, hi);
        if (root.val > hi) return trimBST(root.left, lo, hi);
        root.left  = trimBST(root.left,  lo, hi);
        root.right = trimBST(root.right, lo, hi);
        return root;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  BST-aware DFS. If current value is below lo, its entire LEFT subtree is also
 *  below lo (BST property) → discard, return trimmed RIGHT. Symmetric for > hi.
 *  Otherwise current stays; recurse both subtrees.
 *
 *  Complexity: Time O(n), Space O(h).
 *  Edge cases: range outside everything → null; range covers everything → unchanged.
 *  Pattern: BST DFS with branch pruning. Same template as #938, #270.
 * ============================================================ */
