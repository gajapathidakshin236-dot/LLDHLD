package com.company.DSA;

/**
 * LeetCode #543 - Diameter of Binary Tree
 * Diameter via DFS: at each node, longest path through it = left_height + right_height.
 * Track global max while DFS returns the node's height.
 * Time: O(n)  Space: O(h)
 */
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
