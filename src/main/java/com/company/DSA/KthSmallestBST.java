package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #230 — Kth Smallest Element in a BST
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given the root of a BST and integer k, return the k-th smallest value
 *  (1-indexed).
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
 *   2. Iterative inorder with a stack lets you stop the moment you've popped k items.
 *   3. Follow-up: with frequent inserts/deletes, augment each node with subtree count.
 * ============================================================ */
public class KthSmallestBST {
    public int kthSmallest(TreeNode root, int k) {
        Deque<TreeNode> st = new ArrayDeque<>();
        TreeNode cur = root;
        while (cur != null || !st.isEmpty()) {
            while (cur != null) { st.push(cur); cur = cur.left; }
            cur = st.pop();
            if (--k == 0) return cur.val;
            cur = cur.right;
        }
        return -1;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Iterative inorder: push left chain, pop, then traverse right.
 *  Decrement k on each pop. When k hits 0, current value is the answer.
 *
 *  Why iterative (vs recursive):
 *    Early-exit: stop as soon as we hit the k-th. Recursive harder to early-exit
 *    without exception or returning sentinel.
 *
 *  Complexity: Time O(h + k), Space O(h).
 *  Edge cases: k=1 → leftmost; k=n → rightmost.
 *  Pattern: iterative tree traversal with stack. Same building block:
 *           #173 BST iterator, in-order morris traversal.
 * ============================================================ */
