package com.company.DSA;

import java.util.*;

/**
 * LeetCode #653 - Two Sum IV - BST
 * DFS + HashSet. If k - val seen earlier -> true.
 * Time: O(n)  Space: O(n)
 */
public class TwoSumIVBST {
    public boolean findTarget(TreeNode root, int k) {
        return dfs(root, k, new HashSet<>());
    }
    private boolean dfs(TreeNode n, int k, Set<Integer> seen) {
        if (n == null) return false;
        if (seen.contains(k - n.val)) return true;
        seen.add(n.val);
        return dfs(n.left, k, seen) || dfs(n.right, k, seen);
    }
}
