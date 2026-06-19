package com.company.DSA.bst;

import com.company.DSA.common.TreeNode;

/* ============================================================
 *  LeetCode #538 — Convert BST to Greater Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  Replace each node's value with "self + all greater values" in the BST.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [4,1,6,0,2,5,7,...] → [30,36,21,36,35,26,15,...]
 *  Ex2: [0,null,1] → [1,null,1]
 *
 *  CONSTRAINTS:  0 <= n <= 10^4;  -10^4 <= val <= 10^4;  unique.
 *
 *  HINTS
 *  -----
 *   1. Reverse inorder (R, root, L) visits values largest first.
 *   2. Carry a running sum.
 * ============================================================ */
public class BSTToGreaterTree {

    private int runningSumOfLargerValues = 0;

    public TreeNode convertBST(final TreeNode node) {
        if (node == null) {
            return null;
        }
        convertBST(node.right);

        runningSumOfLargerValues += node.val;
        node.val                  = runningSumOfLargerValues;

        convertBST(node.left);
        return node;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Reverse inorder (R, root, L) descends through values largest first.
 *  Running sum accumulates and overwrites each node's value.
 *
 *  Complexity: Time O(n), Space O(h) recursion.
 *  Pattern: traversal-as-running-accumulation.
 * ============================================================ */
