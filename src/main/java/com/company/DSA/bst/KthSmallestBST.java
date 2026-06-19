package com.company.DSA.bst;

import com.company.DSA.common.TreeNode;
import java.util.*;

/* ============================================================
 *  LeetCode #230 — Kth Smallest Element in a BST
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the k-th smallest value in a BST (1-indexed).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: root=[3,1,4,null,2], k=1 → 1
 *  Ex2: root=[5,3,6,2,4,null,null,1], k=3 → 3
 *  Ex3: root=[2,1,3], k=2 → 2
 *
 *  CONSTRAINTS:  n nodes, 1 <= k <= n <= 10^4;  0 <= val <= 10^4.
 *
 *  HINTS
 *  -----
 *   1. Inorder traversal of BST gives sorted ascending order.
 *   2. Iterative inorder lets you stop the moment you've popped k items.
 * ============================================================ */
public class KthSmallestBST {

    public int kthSmallest(final TreeNode root, int kRemaining) {
        final Deque<TreeNode> ancestorsStack = new ArrayDeque<>();
        TreeNode current = root;

        while (current != null || !ancestorsStack.isEmpty()) {
            while (current != null) {
                ancestorsStack.push(current);
                current = current.left;
            }

            current = ancestorsStack.pop();
            kRemaining--;
            if (kRemaining == 0) {
                return current.val;
            }
            current = current.right;
        }
        throw new IllegalArgumentException("k larger than node count");
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Iterative inorder: push left chain, pop, then traverse right. Decrement k
 *  on each pop. When k hits 0, current value is the answer.
 *
 *  Complexity: Time O(h + k), Space O(h).
 *  Pattern: iterative tree traversal with stack.
 * ============================================================ */
