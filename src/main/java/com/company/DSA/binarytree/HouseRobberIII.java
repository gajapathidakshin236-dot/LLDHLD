package com.company.DSA.binarytree;

import com.company.DSA.common.TreeNode;

/* ============================================================
 *  LeetCode #337 — House Robber III
 * ============================================================
 *  PROBLEM
 *  -------
 *  Houses arranged as a binary tree. Robbing DIRECTLY-CONNECTED houses
 *  triggers police. Return the maximum amount.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [3,2,3,null,3,null,1] → 7   (3+3+1)
 *  Ex2: [3,4,5,1,3,null,1]    → 9   (4+5)
 *
 *  CONSTRAINTS:  1 <= nodes <= 10^4;  0 <= val <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Postorder DP: each call returns [robThis, skipThis].
 * ============================================================ */
public class HouseRobberIII {

    public int rob(final TreeNode root) {
        final int[] decisionsForRoot = bestForSubtree(root);
        return Math.max(decisionsForRoot[0], decisionsForRoot[1]);
    }

    /** Returns [moneyIfWeRobThisNode, moneyIfWeSkipThisNode]. */
    private int[] bestForSubtree(final TreeNode node) {
        if (node == null) {
            return new int[] { 0, 0 };
        }

        final int[] leftDecisions  = bestForSubtree(node.left);
        final int[] rightDecisions = bestForSubtree(node.right);

        final int robThisNode  = node.val + leftDecisions[1] + rightDecisions[1];
        final int skipThisNode = Math.max(leftDecisions[0],  leftDecisions[1])
                               + Math.max(rightDecisions[0], rightDecisions[1]);
        return new int[] { robThisNode, skipThisNode };
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Tree DP with 2-element state per subtree:
 *    [0] = best if we DO rob this node (children must be skipped).
 *    [1] = best if we SKIP this node (children free to rob or not).
 *
 *  Complexity: Time O(n), Space O(h).
 *  Pattern: tree DP with binary state.
 * ============================================================ */
