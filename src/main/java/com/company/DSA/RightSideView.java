package com.company.DSA;

import java.util.*;

/**
 * LeetCode #199 - Right Side View
 * BFS level-by-level; pick the last node per level.
 * Time: O(n)  Space: O(n)
 */
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
