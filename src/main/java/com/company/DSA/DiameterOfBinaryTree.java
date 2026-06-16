package com.company.DSA;

/* ============================================================
 *  LeetCode #543 — Diameter of Binary Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  Diameter = length (in edges) of the LONGEST path between any two nodes.
 *  Path may or may not pass through the root.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,3,4,5]    → 3  (path: 4-2-1-3 or 5-2-1-3, 3 edges)
 *  Ex2: [1,2]          → 1
 *  Ex3: [1]            → 0
 *  Ex4: balanced 7-node BT → 4
 *
 *  CONSTRAINTS:  1 <= nodes <= 10^4;  -100 <= val <= 100.
 *
 *  HINTS
 *  -----
 *   1. At each node, the longest path THROUGH it = leftHeight + rightHeight.
 *   2. Update a global max while DFS returns the node's HEIGHT (max-side+1).
 *   3. Mind units: diameter is edges; height is also edges from leaf side.
 * ============================================================ */
public class DiameterOfBinaryTree {
    int best = 0;
    public int diameterOfBinaryTree(TreeNode root) {
        depth(root);
        return best;
    }
    private int depth(TreeNode n) {
        if (n == null) return 0;
        int L = depth(n.left), R = depth(n.right);
        best = Math.max(best, L + R);
        return 1 + Math.max(L, R);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Post-order DFS returning HEIGHT in edges. While computing, the best diameter
 *  passing through the current node is L + R (left+right heights). We update
 *  a global best, then return 1 + max(L,R) as this node's height to the parent.
 *
 *  Why we don't need to return diameter from DFS:
 *    Local diameter is just a snapshot, but we want the GLOBAL maximum across
 *    all nodes. A class field captures that cleanly.
 *
 *  Complexity: Time O(n), Space O(h).
 *  Edge cases: single node → diameter 0; left/right skewed line.
 *  Pattern: "DFS returns one quantity, side-effect tracks another" — appears in
 *           max path sum (#124), house robber III (#337), good nodes (#1448).
 * ============================================================ */
