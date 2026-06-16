package com.company.DSA;

import java.util.*;

/**
 * LeetCode #863 - All Nodes Distance K in Binary Tree
 * Build parent map, then BFS from target up to depth k.
 * Time: O(n)  Space: O(n)
 */
public class NodesAtDistanceK {
    public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
        Map<TreeNode, TreeNode> par = new HashMap<>();
        mapParents(root, null, par);
        Set<TreeNode> visited = new HashSet<>();
        Deque<TreeNode> q = new ArrayDeque<>();
        q.offer(target); visited.add(target);
        int d = 0;
        while (!q.isEmpty()) {
            if (d == k) {
                List<Integer> res = new ArrayList<>();
                for (TreeNode n : q) res.add(n.val);
                return res;
            }
            int sz = q.size();
            for (int i = 0; i < sz; i++) {
                TreeNode n = q.poll();
                for (TreeNode nb : new TreeNode[]{n.left, n.right, par.get(n)}) {
                    if (nb != null && visited.add(nb)) q.offer(nb);
                }
            }
            d++;
        }
        return new ArrayList<>();
    }
    private void mapParents(TreeNode n, TreeNode p, Map<TreeNode, TreeNode> m) {
        if (n == null) return;
        m.put(n, p);
        mapParents(n.left, n, m);
        mapParents(n.right, n, m);
    }
}
