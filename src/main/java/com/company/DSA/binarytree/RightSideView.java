package com.company.DSA.binarytree;

import com.company.DSA.common.TreeNode;
import java.util.*;

/* ============================================================
 *  LeetCode #199 — Binary Tree Right Side View
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the values of the rightmost node at each level, top to bottom.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,3,null,5,null,4] → [1,3,4]
 *  Ex2: [1,null,3]            → [1,3]
 *  Ex3: []                    → []
 *
 *  CONSTRAINTS:  0 <= nodes <= 100;  -100 <= val <= 100.
 *
 *  HINTS
 *  -----
 *   1. BFS per level, pick the LAST node enqueued of that level.
 * ============================================================ */
public class RightSideView {

    public List<Integer> rightSideView(final TreeNode root) {
        final List<Integer> rightmostPerLevel = new ArrayList<>();
        if (root == null) {
            return rightmostPerLevel;
        }

        final Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            final int nodesInThisLevel = queue.size();

            for (int positionInLevel = 0; positionInLevel < nodesInThisLevel; positionInLevel++) {
                final TreeNode currentNode = queue.poll();

                if (positionInLevel == nodesInThisLevel - 1) {
                    rightmostPerLevel.add(currentNode.val);
                }

                if (currentNode.left  != null) { queue.offer(currentNode.left);  }
                if (currentNode.right != null) { queue.offer(currentNode.right); }
            }
        }
        return rightmostPerLevel;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  BFS by level (size-snapshot trick). Within each level loop, the LAST popped
 *  node is the rightmost visible one — append its value.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: BFS level scan; "last vs first per level" toggles for different views.
 * ============================================================ */
