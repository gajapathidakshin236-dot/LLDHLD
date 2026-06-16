package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #102 — Binary Tree Level Order Traversal
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return a list of node values, level by level (left to right).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [3,9,20,null,null,15,7]   → [[3],[9,20],[15,7]]
 *  Ex2: [1]                       → [[1]]
 *  Ex3: []                        → []
 *  Ex4: skewed 1->2->3 (left)     → [[1],[2],[3]]
 *
 *  CONSTRAINTS:  0 <= nodes <= 2000;  -1000 <= val <= 1000.
 *
 *  HINTS
 *  -----
 *   1. BFS with a queue is the natural fit.
 *   2. Snapshot the queue size at the start of each level — that's how many
 *      nodes belong to the current level.
 *   3. Build per-level list inside that inner loop.
 * ============================================================ */
public class LevelOrderTraversal {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        Deque<TreeNode> q = new ArrayDeque<>();
        q.offer(root);
        while (!q.isEmpty()) {
            int sz = q.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < sz; i++) {
                TreeNode n = q.poll();
                level.add(n.val);
                if (n.left != null)  q.offer(n.left);
                if (n.right != null) q.offer(n.right);
            }
            res.add(level);
        }
        return res;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Classic BFS. The size-snapshot trick lets us separate levels cleanly
 *  without storing depth in the queue.
 *
 *  Why size snapshot:
 *    At any moment the queue may contain nodes from multiple levels because
 *    we just enqueued children. Snapshotting `sz` BEFORE the inner loop fixes
 *    the count of current-level nodes; everything enqueued during the loop is
 *    "next level" and will be picked up next iteration.
 *
 *  DFS alt: recursive with depth → also O(n), simpler code but recursion stack.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Edge cases: empty → []; skewed → each level has 1 node.
 *  Pattern: BFS by level — used in zigzag (#103), right side view (#199),
 *           average per level, max width (#662).
 * ============================================================ */
