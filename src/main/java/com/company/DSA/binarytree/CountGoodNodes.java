package com.company.DSA.binarytree;

import com.company.DSA.common.TreeNode;

/* ============================================================
 *  LeetCode #1448 — Count Good Nodes in Binary Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  A node X is "good" if there's no node on the path from root to X with a
 *  value GREATER than X.val. Count good nodes.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [3,1,4,3,null,1,5] → 4
 *  Ex2: [3,3,null,4,2]     → 3
 *  Ex3: [1]                → 1
 *
 *  CONSTRAINTS:  1 <= nodes <= 10^5;  -10^4 <= val <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. DFS carrying maxSoFar on the path.
 *   2. Node is good iff node.val >= maxSoFar.
 * ============================================================ */
public class CountGoodNodes {

    public int goodNodes(final TreeNode root) {
        return countGood(root, Integer.MIN_VALUE);
    }

    private int countGood(final TreeNode node, final int maxOnPathSoFar) {
        if (node == null) {
            return 0;
        }
        final int countAtThisNode = (node.val >= maxOnPathSoFar) ? 1 : 0;
        final int newMaxOnPath    = Math.max(maxOnPathSoFar, node.val);

        return countAtThisNode
             + countGood(node.left,  newMaxOnPath)
             + countGood(node.right, newMaxOnPath);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  DFS with a running max along the root-to-current path. A node qualifies
 *  if its value is at least as large as any previously seen on this path.
 *
 *  Complexity: Time O(n), Space O(h) stack.
 *  Pattern: state carried down the recursion.
 * ============================================================ */
