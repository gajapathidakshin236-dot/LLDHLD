package com.company.DSA.srivatsanDSA;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 26
 *  Problem: Binary Tree Right Side View
 *  Return values of nodes visible from the right side, top to bottom.
 *
 *  APPROACH (from notes):
 *    Two options shown in notes:
 *      (1) DFS — for each level, add the FIRST node encountered when
 *          visiting right child before left.
 *      (2) BFS — for each level, push the LAST node value of the level.
 *    This file uses BFS option (clearest in interview).
 * ============================================================ */
public class BinaryTreeRightSideView {

    public List<Integer> rightSideView(final TreeNode root) {
        final List<Integer> rightmostPerLevel = new ArrayList<>();
        if (root == null) {
            return rightmostPerLevel;
        }

        final Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            final int levelSize = queue.size();
            for (int position = 0; position < levelSize; position++) {
                final TreeNode currentNode = queue.poll();

                if (position == levelSize - 1) {
                    rightmostPerLevel.add(currentNode.val);
                }
                if (currentNode.left  != null) { queue.offer(currentNode.left);  }
                if (currentNode.right != null) { queue.offer(currentNode.right); }
            }
        }
        return rightmostPerLevel;
    }
}
