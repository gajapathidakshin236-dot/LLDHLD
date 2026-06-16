package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #863 — All Nodes Distance K in Binary Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given a binary tree, a target node, and an integer K, return all nodes
 *  that are EXACTLY K edges from the target.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: root=[3,5,1,6,2,0,8,null,null,7,4], target=5, k=2 → [7,4,1]
 *  Ex2: root=[1], target=1, k=3 → []
 *  Ex3: target itself with k=0 → [target.val]
 *
 *  CONSTRAINTS:  1 <= nodes <= 500;  0 <= val <= 500.
 *
 *  HINTS
 *  -----
 *   1. Trees don't allow upward traversal — build a parent map first.
 *   2. BFS from target as if it were an undirected graph (left, right, parent).
 *   3. Stop after K levels.
 * ============================================================ */
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

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Convert the tree into an undirected graph by recording each node's parent.
 *  BFS from target, with neighbors = {left, right, parent}. Use a visited set
 *  so we don't bounce back. After k levels of BFS, the current queue holds
 *  all nodes at distance k.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Edge cases: k=0 → just target; target with no neighbors at distance k → [].
 *  Pattern: tree → graph + BFS. Same trick: distance N in graph.
 * ============================================================ */
