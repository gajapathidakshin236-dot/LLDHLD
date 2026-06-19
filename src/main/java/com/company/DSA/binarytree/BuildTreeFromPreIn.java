package com.company.DSA.binarytree;

import com.company.DSA.common.TreeNode;
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
 *  CONSTRAINTS:  1 <= n <= 3000; values unique.
 *
 *  HINTS
 *  -----
 *   1. Preorder first element is the ROOT of the (sub)tree.
 *   2. Find that root in inorder → splits left/right subtrees.
 *   3. HashMap inorder values → indices for O(1) lookup.
 * ============================================================ */
public class BuildTreeFromPreIn {

    private int preorderCursor = 0;
    private final Map<Integer, Integer> inorderIndexOf = new HashMap<>();

    public TreeNode buildTree(final int[] preorder, final int[] inorder) {
        for (int index = 0; index < inorder.length; index++) {
            inorderIndexOf.put(inorder[index], index);
        }
        return build(preorder, 0, inorder.length - 1);
    }

    private TreeNode build(final int[] preorder,
                           final int inorderLeft,
                           final int inorderRight) {
        if (inorderLeft > inorderRight) {
            return null;
        }

        final int rootValue        = preorder[preorderCursor++];
        final TreeNode root        = new TreeNode(rootValue);
        final int rootIndexInorder = inorderIndexOf.get(rootValue);

        root.left  = build(preorder, inorderLeft,            rootIndexInorder - 1);
        root.right = build(preorder, rootIndexInorder + 1,   inorderRight);
        return root;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Two invariants:
 *    (a) preorder consumes root → left subtree → right subtree, in order.
 *        So a single cursor walks preorder once.
 *    (b) inorder splits subtree boundaries — left of root index is left subtree.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: rebuild structure from two traversal orders.
 * ============================================================ */
