package com.company.DSA;

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
 *  Ex4: [1,2,3]               → [1,3]
 *
 *  CONSTRAINTS:  0 <= nodes <= 100;  -100 <= val <= 100.
 *
 *  HINTS
 *  -----
 *   1. BFS per level, pick the LAST node enqueued of that level.
 *   2. Or DFS visiting right child first; first visit per depth wins.
 * ============================================================ */
public class RightSideView {
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Deque<TreeNode> q = new ArrayDeque<>();
        q.offer(root);
        while (!q.isEmpty()) {
            int sz = q.size();
            for (int i = 0; i < sz; i++) {
                TreeNode n = q.poll();
                if (i == sz - 1) res.add(n.val);
                if (n.left != null)  q.offer(n.left);
                if (n.right != null) q.offer(n.right);
            }
        }
        return res;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  BFS by level (size-snapshot trick like #102). Within each level loop,
 *  the LAST popped node is the rightmost visible one — append its value.
 *
 *  DFS alternative:
 *    Visit right child before left, carry depth. If depth == res.size() append
 *    (first arrival at that depth = right-most due to traversal order).
 *
 *  Complexity: Time O(n), Space O(n) for queue (or O(h) for recursion).
 *  Edge cases: empty; left-only chain — rightmost equals leftmost.
 *  Pattern: BFS level scan; "last vs first per level" toggles for different views.
 * ============================================================ */
