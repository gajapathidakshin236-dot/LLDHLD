package com.company.DSA;

import java.util.*;

/**
 * LeetCode #662 - Maximum Width of Binary Tree
 * BFS with index labels (2*i, 2*i+1). Width = lastIdx - firstIdx + 1 at each level.
 * Renormalize indices to avoid overflow.
 * Time: O(n)  Space: O(n)
 */
public class MaxWidthBinaryTree {
    public int widthOfBinaryTree(TreeNode root) {
        if (root == null) return 0;
        Deque<long[]> q = new ArrayDeque<>(); // (node, idx)
        // We can't directly store TreeNode in long[]; pair with separate deque:
        Deque<TreeNode> nq = new ArrayDeque<>();
        Deque<Long> iq = new ArrayDeque<>();
        nq.offer(root); iq.offer(1L);
        int best = 0;
        while (!nq.isEmpty()) {
            int sz = nq.size();
            long first = iq.peek(), last = first;
            for (int i = 0; i < sz; i++) {
                TreeNode n = nq.poll();
                long idx = iq.poll() - first; // normalize
                last = idx + first;
                if (n.left  != null) { nq.offer(n.left);  iq.offer(2 * idx); }
                if (n.right != null) { nq.offer(n.right); iq.offer(2 * idx + 1); }
            }
            best = (int) Math.max(best, last - first + 1);
        }
        return best;
    }
}
