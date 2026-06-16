package com.company.DSA;

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
 *   1. DFS down to LEAF only. Internal nodes don't count even if sum hits.
 *   2. Carry a remaining count; decrement at each node.
 *   3. Backtrack by removing the last element from the path list.
 * ============================================================ */
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

/* ============================================================
 *  APPROACH
 * ============================================================
 *  DFS with explicit "leaf" check. Push value on entry, pop on exit
 *  (backtracking) so the same list is reused across all paths.
 *
 *  Why snapshot at leaf:
 *    The path is a mutable list — must clone it before storing, otherwise
 *    later mutations would overwrite stored entries.
 *
 *  Why subtract from rem instead of accumulating sum:
 *    Same logic, just lets us compare rem == n.val at leaf directly.
 *
 *  Complexity: Time O(n^2) worst (every node on every path), Space O(h) recursion.
 *  Edge cases: empty tree → []; single node equal to target → [[val]].
 *  Pattern: backtracking on trees with reusable path buffer.
 * ============================================================ */
