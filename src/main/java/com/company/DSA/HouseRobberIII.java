package com.company.DSA;

/**
 * LeetCode #337 - House Robber III
 * Postorder DP: per node return [robThis, skipThis]. rob = val + skipL + skipR; skip = max sides.
 * Time: O(n)  Space: O(h)
 */
public class HouseRobberIII {
    public int rob(TreeNode root) {
        int[] r = dfs(root);
        return Math.max(r[0], r[1]);
    }
    private int[] dfs(TreeNode n) {
        if (n == null) return new int[2];
        int[] L = dfs(n.left), R = dfs(n.right);
        int rob = n.val + L[1] + R[1];
        int skip = Math.max(L[0], L[1]) + Math.max(R[0], R[1]);
        return new int[]{rob, skip};
    }
}
