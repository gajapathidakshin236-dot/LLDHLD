package com.company.DSA.bst;

import com.company.DSA.common.TreeNode;

/* ============================================================
 *  LeetCode #450 — Delete Node in BST
 * ============================================================
 *  PROBLEM
 *  -------
 *  Delete the node with given value (if exists) and return the resulting BST.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: root=[5,3,6,2,4,null,7], key=3 → [5,4,6,2,null,null,7]
 *  Ex2: same, key=0 → unchanged
 *  Ex3: root=[], key=5 → []
 *
 *  CONSTRAINTS:  0 <= n <= 10^4; values unique.
 *
 *  HINTS
 *  -----
 *   1. Recursive find: go left/right by BST property.
 *   2. On find: 0/1 child → return the child; 2 children → replace value with
 *      inorder successor, then delete successor.
 * ============================================================ */
public class DeleteNodeInBST {

    public TreeNode deleteNode(final TreeNode root, final int keyToRemove) {
        if (root == null) {
            return null;
        }

        if (keyToRemove < root.val) {
            root.left = deleteNode(root.left, keyToRemove);
            return root;
        }
        if (keyToRemove > root.val) {
            root.right = deleteNode(root.right, keyToRemove);
            return root;
        }

        // Found node to delete.
        if (root.left == null)  { return root.right; }
        if (root.right == null) { return root.left;  }

        final TreeNode inorderSuccessor = leftmost(root.right);
        root.val   = inorderSuccessor.val;
        root.right = deleteNode(root.right, inorderSuccessor.val);
        return root;
    }

    private TreeNode leftmost(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Standard BST delete with successor replacement.
 *  When the node has two children, the smallest value greater than it (the
 *  inorder successor, leftmost of right subtree) takes its place. Then we
 *  recursively delete that successor.
 *
 *  Complexity: Time O(h), Space O(h) recursion.
 *  Pattern: classic BST delete.
 * ============================================================ */
