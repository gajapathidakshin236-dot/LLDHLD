package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #105 — Construct Binary Tree from Preorder and Inorder
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given preorder and inorder traversals (no duplicates), reconstruct the tree.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: preorder=[3,9,20,15,7], inorder=[9,3,15,20,7] → [3,9,20,null,null,15,7]
 *  Ex2: preorder=[-1], inorder=[-1] → [-1]
 *
 *  CONSTRAINTS:  1 <= n <= 3000; values unique; both arrays describe same tree.
 *
 *  HINTS
 *  -----
 *   1. Preorder first element is the ROOT of the (sub)tree.
 *   2. Find that root in inorder → splits left/right subtrees.
 *   3. HashMap inorder values → indices for O(1) lookup.
 *   4. Maintain a moving pointer over preorder.
 * ============================================================ */
public class BuildTreeFromPreIn {
    int pre = 0;
    Map<Integer,Integer> idx = new HashMap<>();
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        for (int i = 0; i < inorder.length; i++) idx.put(inorder[i], i);
        return build(preorder, 0, inorder.length - 1);
    }
    private TreeNode build(int[] preorder, int l, int r) {
        if (l > r) return null;
        int v = preorder[pre++];
        TreeNode n = new TreeNode(v);
        int mid = idx.get(v);
        n.left  = build(preorder, l, mid - 1);
        n.right = build(preorder, mid + 1, r);
        return n;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two invariants:
 *    (a) preorder consumes root → left subtree → right subtree, in order.
 *        So a single pointer `pre` walks preorder once.
 *    (b) inorder splits subtree boundaries — left of root index is left subtree.
 *
 *  HashMap of inorder values → index avoids O(n) linear searches per node.
 *
 *  Complexity: Time O(n), Space O(n) map + O(h) recursion.
 *  Edge cases: single node; left-only or right-only chain.
 *  Pattern: rebuild structure from two traversal orders. Sibling: #106 (in+post).
 * ============================================================ */
