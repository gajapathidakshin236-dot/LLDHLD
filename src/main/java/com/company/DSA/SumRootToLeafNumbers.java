package com.company.DSA;

/**
 * LeetCode #129 - Sum Root to Leaf Numbers
 * DFS with running number = cur*10 + val. At leaf, add.
 * Time: O(n)  Space: O(h)
 */
public class SumRootToLeafNumbers {
    public int sumNumbers(TreeNode root) { return dfs(root, 0); }
    private int dfs(TreeNode n, int cur) {
        if (n == null) return 0;
        cur = cur * 10 + n.val;
        if (n.left == null && n.right == null) return cur;
        return dfs(n.left, cur) + dfs(n.right, cur);
    }
}
