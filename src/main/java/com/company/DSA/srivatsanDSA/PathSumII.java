package com.company.DSA.srivatsanDSA;

import java.util.ArrayList;
import java.util.List;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 24-25
 *  Problem: Path Sum II — all root-to-leaf paths summing to target.
 *
 *  APPROACH (from notes):
 *    dfs(root, target, path, result):
 *       if root == null → return
 *       path.add(root.val)
 *       if leaf and target - root.val == 0 → result.add(copy of path)
 *       else dfs(left, target - root.val, ...); dfs(right, target - root.val, ...)
 *       path.remove(last)   // backtrack
 * ============================================================ */
public class PathSumII {

    public List<List<Integer>> pathSum(final TreeNode root, final int targetSum) {
        final List<List<Integer>> allPaths = new ArrayList<>();
        collectPaths(root, targetSum, new ArrayList<>(), allPaths);
        return allPaths;
    }

    private void collectPaths(final TreeNode node,
                              final int remainingSum,
                              final List<Integer> currentPath,
                              final List<List<Integer>> output) {
        if (node == null) {
            return;
        }
        currentPath.add(node.val);
        final boolean isLeaf = node.left == null && node.right == null;

        if (isLeaf && remainingSum == node.val) {
            output.add(new ArrayList<>(currentPath));
        } else {
            collectPaths(node.left,  remainingSum - node.val, currentPath, output);
            collectPaths(node.right, remainingSum - node.val, currentPath, output);
        }
        currentPath.remove(currentPath.size() - 1);
    }
}
