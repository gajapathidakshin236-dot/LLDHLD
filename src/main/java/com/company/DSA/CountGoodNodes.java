package com.company.DSA;

/**
 * LeetCode #1448 - Count Good Nodes in Binary Tree
 * DFS carrying max-so-far on path. Good if val >= max.
 * Time: O(n)  Space: O(h)
 */
public class CountGoodNodes {
    public int goodNodes(TreeNode root) { return dfs(root, Integer.MIN_VALUE); }
    private int dfs(TreeNode n, int max) {
        if (n == null) return 0;
        int add = n.val >= max ? 1 : 0;
        int nm = Math.max(max, n.val);
        return add + dfs(n.left, nm) + dfs(n.right, nm);
    }
}
