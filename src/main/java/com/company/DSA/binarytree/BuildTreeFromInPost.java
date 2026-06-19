package com.company.DSA.binarytree;

import com.company.DSA.common.TreeNode;
import java.util.*;

/* ============================================================
 *  LeetCode #106 — Construct Binary Tree from Inorder and Postorder
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given inorder and postorder traversals (unique values), rebuild the tree.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: inorder=[9,3,15,20,7], postorder=[9,15,7,20,3] → [3,9,20,null,null,15,7]
 *  Ex2: inorder=[-1], postorder=[-1] → [-1]
 *
 *  CONSTRAINTS:  1 <= n <= 3000; unique values.
 *
 *  HINTS
 *  -----
 *   1. Postorder's LAST element is the ROOT.
 *   2. Locate root in inorder → split left/right subtrees.
 *   3. Recurse RIGHT first then LEFT (postorder consumed reverse).
 * ============================================================ */
public class BuildTreeFromInPost {

    private int postorderCursor;
    private int[] postorder;
    private final Map<Integer, Integer> inorderIndexOf = new HashMap<>();

    public TreeNode buildTree(final int[] inorder, final int[] postorder) {
        this.postorder       = postorder;
        this.postorderCursor = postorder.length - 1;

        for (int index = 0; index < inorder.length; index++) {
            inorderIndexOf.put(inorder[index], index);
        }
        return build(0, inorder.length - 1);
    }

    private TreeNode build(final int inorderLeft, final int inorderRight) {
        if (inorderLeft > inorderRight) {
            return null;
        }

        final int rootValue        = postorder[postorderCursor--];
        final TreeNode root        = new TreeNode(rootValue);
        final int rootIndexInorder = inorderIndexOf.get(rootValue);

        root.right = build(rootIndexInorder + 1, inorderRight);
        root.left  = build(inorderLeft,          rootIndexInorder - 1);
        return root;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Symmetric to #105. Postorder consumed RIGHT-to-LEFT, so we build the right
 *  subtree first, then the left.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: reconstruct tree from two traversal orders.
 * ============================================================ */
