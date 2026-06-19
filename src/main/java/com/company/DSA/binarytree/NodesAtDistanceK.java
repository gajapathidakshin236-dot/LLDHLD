package com.company.DSA.binarytree;

import com.company.DSA.common.TreeNode;
import java.util.*;

/* ============================================================
 *  LeetCode #863 — All Nodes Distance K in Binary Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return all nodes that are EXACTLY K edges from a given target node.
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
 *   1. Build a parent map first so we can traverse up.
 *   2. BFS from target as if undirected graph (left, right, parent).
 *   3. Stop after K levels.
 * ============================================================ */
public class NodesAtDistanceK {

    public List<Integer> distanceK(final TreeNode root,
                                   final TreeNode target,
                                   final int distance) {
        final Map<TreeNode, TreeNode> parentOf = new HashMap<>();
        recordParents(root, null, parentOf);

        final Set<TreeNode> visited = new HashSet<>();
        visited.add(target);

        final Deque<TreeNode> bfsFrontier = new ArrayDeque<>();
        bfsFrontier.offer(target);

        int currentDistance = 0;
        while (!bfsFrontier.isEmpty()) {
            if (currentDistance == distance) {
                return bfsFrontier.stream().map(node -> node.val).collect(java.util.stream.Collectors.toList());
            }
            final int nodesAtThisDistance = bfsFrontier.size();

            for (int popCount = 0; popCount < nodesAtThisDistance; popCount++) {
                final TreeNode currentNode = bfsFrontier.poll();
                final TreeNode[] neighbors = {
                        currentNode.left,
                        currentNode.right,
                        parentOf.get(currentNode)
                };
                for (final TreeNode neighbor : neighbors) {
                    if (neighbor != null && visited.add(neighbor)) {
                        bfsFrontier.offer(neighbor);
                    }
                }
            }
            currentDistance++;
        }
        return Collections.emptyList();
    }

    private void recordParents(final TreeNode node,
                               final TreeNode parent,
                               final Map<TreeNode, TreeNode> parentOf) {
        if (node == null) {
            return;
        }
        parentOf.put(node, parent);
        recordParents(node.left,  node, parentOf);
        recordParents(node.right, node, parentOf);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Convert the tree into an undirected graph via a parent map. BFS from target,
 *  with neighbors = {left, right, parent}. Visited set prevents bouncing back.
 *  After K levels of BFS, the current frontier holds all nodes at distance K.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: tree → graph + BFS.
 * ============================================================ */
