package com.company.DSA.binarytree;

import com.company.DSA.common.TreeNode;

/* ============================================================
 *  LeetCode #543 — Diameter of Binary Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  Length (in edges) of the LONGEST path between any two nodes.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,3,4,5] → 3
 *  Ex2: [1,2]       → 1
 *  Ex3: [1]         → 0
 *
 *  CONSTRAINTS:  1 <= nodes <= 10^4;  -100 <= val <= 100.
 *
 *  HINTS
 *  -----
 *   1. At each node, longest path THROUGH it = leftHeight + rightHeight.
 *   2. Track global max while DFS returns the node's HEIGHT.
 * ============================================================ */
public class DiameterOfBinaryTree {

    private int longestPathSeen = 0;

    public int diameterOfBinaryTree(final TreeNode root) {
        computeHeight(root);
        return longestPathSeen;
    }

    private int computeHeight(final TreeNode node) {
        if (node == null) {
            return 0;
        }
        final int leftHeight  = computeHeight(node.left);
        final int rightHeight = computeHeight(node.right);

        longestPathSeen = Math.max(longestPathSeen, leftHeight + rightHeight);
        return 1 + Math.max(leftHeight, rightHeight);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Post-order DFS returning HEIGHT in edges. While computing, the best
 *  diameter passing through the current node is leftHeight + rightHeight.
 *  Field captures the global maximum.
 *
 *  Complexity: Time O(n), Space O(h).
 *  Pattern: "DFS returns one quantity, side-effect tracks another."
 * ============================================================ */
