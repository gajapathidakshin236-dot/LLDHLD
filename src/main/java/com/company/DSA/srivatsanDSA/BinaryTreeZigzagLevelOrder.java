package com.company.DSA.srivatsanDSA;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 27
 *  Problem: Binary Tree Zigzag Level Order Traversal
 *  Output: alternate levels left→right then right→left.
 *
 *  APPROACH (from notes):
 *    Same as level-order traversal but reverse every alternate level (odd levels).
 *    Use a flag or build into LinkedList and use addFirst/addLast.
 *    Deque-based: addFirst when level is odd, addLast otherwise.
 * ============================================================ */
public class BinaryTreeZigzagLevelOrder {

    public List<List<Integer>> zigzagLevelOrder(final TreeNode root) {
        final List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        final Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        boolean leftToRight = true;

        while (!queue.isEmpty()) {
            final int levelSize = queue.size();
            final Deque<Integer> currentLevel = new ArrayDeque<>();

            for (int position = 0; position < levelSize; position++) {
                final TreeNode currentNode = queue.poll();

                if (leftToRight) {
                    currentLevel.offerLast(currentNode.val);
                } else {
                    currentLevel.offerFirst(currentNode.val);
                }

                if (currentNode.left  != null) { queue.offer(currentNode.left);  }
                if (currentNode.right != null) { queue.offer(currentNode.right); }
            }
            result.add(new ArrayList<>(currentLevel));
            leftToRight = !leftToRight;
        }
        return result;
    }
}
