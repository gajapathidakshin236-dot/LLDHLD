package com.company.DSA.binarytree;

import com.company.DSA.common.TreeNode;
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
 *
 *  CONSTRAINTS:  0 <= nodes <= 2000;  -1000 <= val <= 1000.
 *
 *  HINTS
 *  -----
 *   1. BFS with a queue.
 *   2. Snapshot queue size at the start of each level.
 *   3. Build per-level list inside the inner loop.
 * ============================================================ */
public class LevelOrderTraversal {

    public List<List<Integer>> levelOrder(final TreeNode root) {
        final List<List<Integer>> levels = new ArrayList<>();
        if (root == null) {
            return levels;
        }

        final Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            final int nodesInThisLevel = queue.size();
            final List<Integer> currentLevel = new ArrayList<>(nodesInThisLevel);

            for (int index = 0; index < nodesInThisLevel; index++) {
                final TreeNode currentNode = queue.poll();
                currentLevel.add(currentNode.val);

                if (currentNode.left  != null) { queue.offer(currentNode.left);  }
                if (currentNode.right != null) { queue.offer(currentNode.right); }
            }
            levels.add(currentLevel);
        }
        return levels;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Classic BFS. The size-snapshot trick lets us separate levels cleanly
 *  without storing depth in the queue.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: BFS by level — used in zigzag (#103), right side view (#199).
 * ============================================================ */
