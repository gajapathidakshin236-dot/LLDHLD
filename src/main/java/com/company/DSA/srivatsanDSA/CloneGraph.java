package com.company.DSA.srivatsanDSA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 32
 *  Problem: Clone Graph (deep copy of a connected undirected graph)
 *
 *  APPROACH (from notes):
 *    HashMap<original Node, clone Node> to remember already-cloned nodes.
 *    DFS:
 *      if node in map → return map.get(node)
 *      clone = new Node(node.val)
 *      map.put(node, clone)
 *      for nb in node.neighbors:
 *          clone.neighbors.add(dfs(nb))
 *      return clone
 *    Map plays dual role: visited-set + clone-lookup, preventing infinite recursion on cycles.
 * ============================================================ */
public class CloneGraph {

    public static class Node {
        public int val;
        public List<Node> neighbors;

        public Node()                  { this.neighbors = new ArrayList<>(); }
        public Node(final int v)       { this.val = v; this.neighbors = new ArrayList<>(); }
        public Node(final int v, final List<Node> n) { this.val = v; this.neighbors = n; }
    }

    public Node cloneGraph(final Node originalRoot) {
        if (originalRoot == null) {
            return null;
        }
        return deepCopy(originalRoot, new HashMap<>());
    }

    private Node deepCopy(final Node originalNode, final Map<Node, Node> cloneByOriginal) {
        final Node alreadyCloned = cloneByOriginal.get(originalNode);
        if (alreadyCloned != null) {
            return alreadyCloned;
        }
        final Node freshClone = new Node(originalNode.val);
        cloneByOriginal.put(originalNode, freshClone);

        for (final Node neighborOriginal : originalNode.neighbors) {
            freshClone.neighbors.add(deepCopy(neighborOriginal, cloneByOriginal));
        }
        return freshClone;
    }
}
