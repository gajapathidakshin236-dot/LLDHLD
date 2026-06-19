package com.company.DSA.binarytree;

import com.company.DSA.common.TreeNode;
import java.util.*;

/* ============================================================
 *  LeetCode #113 — Path Sum II
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return ALL root-to-leaf paths whose values sum to targetSum.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: root=[5,4,8,11,null,13,4,7,2,null,null,5,1], target=22
 *       → [[5,4,11,2],[5,8,4,5]]
 *  Ex2: root=[1,2,3], target=5 → []
 *  Ex3: root=[1,2], target=0 → []
 *
 *  CONSTRAINTS:  0 <= nodes <= 5000;  -1000 <= val <= 1000.
 *
 *  HINTS
 *  -----
 *   1. DFS down to LEAF only.
 *   2. Backtrack by removing the last element from the path list.
 * ============================================================ */
public class PathSumII {

    public List<List<Integer>> pathSum(final TreeNode root, final int targetSum) {
        final List<List<Integer>> allMatchingPaths = new ArrayList<>();
        collectPaths(root, targetSum, new ArrayList<>(), allMatchingPaths);
        return allMatchingPaths;
    }

    private void collectPaths(final TreeNode node,
                              final int remainingSum,
                              final List<Integer> currentPath,
                              final List<List<Integer>> output) {
        if (node == null) {
            return;
        }

        currentPath.add(node.val);
        final boolean isLeaf = (node.left == null && node.right == null);

        if (isLeaf && remainingSum == node.val) {
            output.add(new ArrayList<>(currentPath));
        } else {
            collectPaths(node.left,  remainingSum - node.val, currentPath, output);
            collectPaths(node.right, remainingSum - node.val, currentPath, output);
        }

        currentPath.remove(currentPath.size() - 1);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  DFS with explicit "leaf" check. Push value on entry, pop on exit
 *  (backtracking) so the same list is reused across all paths.
 *
 *  Complexity: Time O(n^2) worst (copying paths), Space O(h) recursion.
 *  Pattern: backtracking on trees with reusable path buffer.
 * ============================================================ */
