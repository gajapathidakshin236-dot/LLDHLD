package com.company.DSA;

import java.util.*;

/**
 * LeetCode #113 - Path Sum II
 * Backtrack from root; on leaf with remaining==0, snapshot path.
 * Time: O(n^2) worst (copying paths)  Space: O(h)
 */
public class PathSumII {
    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(root, targetSum, new ArrayList<>(), res);
        return res;
    }
    private void dfs(TreeNode n, int rem, List<Integer> path, List<List<Integer>> res) {
        if (n == null) return;
        path.add(n.val);
        if (n.left == null && n.right == null && rem == n.val) res.add(new ArrayList<>(path));
        dfs(n.left,  rem - n.val, path, res);
        dfs(n.right, rem - n.val, path, res);
        path.remove(path.size() - 1);
    }
}
