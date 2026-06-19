package com.company.DSA.binarytree;

import com.company.DSA.common.TreeNode;
import java.util.*;

/* ============================================================
 *  LeetCode #662 — Maximum Width of Binary Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  Width of a level = #cells between left-most & right-most non-null nodes
 *  on that level, INCLUDING null gaps as if the tree were complete.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,3,2,5,3,null,9]                              → 4
 *  Ex2: [1,3,2,5,null,null,9,6,null,7]                  → 8
 *  Ex3: [1,3,2,5]                                       → 2
 *
 *  CONSTRAINTS:  1 <= nodes <= 3000;  -100 <= val <= 100.
 *
 *  HINTS
 *  -----
 *   1. Label each node by position index as if tree were complete.
 *   2. Per level, width = lastIndex - firstIndex + 1.
 *   3. Renormalize indices each level to avoid overflow.
 * ============================================================ */
public class MaxWidthBinaryTree {

    public int widthOfBinaryTree(final TreeNode root) {
        if (root == null) {
            return 0;
        }

        final Deque<TreeNode> nodeQueue  = new ArrayDeque<>();
        final Deque<Long>     indexQueue = new ArrayDeque<>();

        nodeQueue.offer(root);
        indexQueue.offer(1L);

        int maxWidthSeen = 0;

        while (!nodeQueue.isEmpty()) {
            final int nodesInThisLevel = nodeQueue.size();
            final long firstIndexThisLevel = indexQueue.peek();
            long lastIndexThisLevel = firstIndexThisLevel;

            for (int position = 0; position < nodesInThisLevel; position++) {
                final TreeNode currentNode = nodeQueue.poll();
                final long normalizedIndex = indexQueue.poll() - firstIndexThisLevel;
                lastIndexThisLevel = normalizedIndex + firstIndexThisLevel;

                if (currentNode.left != null) {
                    nodeQueue.offer(currentNode.left);
                    indexQueue.offer(2 * normalizedIndex);
                }
                if (currentNode.right != null) {
                    nodeQueue.offer(currentNode.right);
                    indexQueue.offer(2 * normalizedIndex + 1);
                }
            }
            maxWidthSeen = (int) Math.max(maxWidthSeen, lastIndexThisLevel - firstIndexThisLevel + 1);
        }
        return maxWidthSeen;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  BFS with positional labels: each node has a unique index as if the tree
 *  were complete (root=1, left=2i, right=2i+1). Width = right_idx - left_idx + 1.
 *  Renormalize by subtracting the FIRST index per level to prevent overflow.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: positional labeling on trees.
 * ============================================================ */
