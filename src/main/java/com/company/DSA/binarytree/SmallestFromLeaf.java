package com.company.DSA.binarytree;

import com.company.DSA.common.TreeNode;

/* ============================================================
 *  LeetCode #988 — Smallest String Starting From Leaf
 * ============================================================
 *  PROBLEM
 *  -------
 *  Each node value is 0..25 representing 'a'..'z'. Concatenate values along
 *  paths from any LEAF to root; return the lex-smallest such string.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [0,1,2,3,4,3,4]          → "dba"
 *  Ex2: [25,1,3,1,3,0,2]         → "adz"
 *  Ex3: [2,2,1,null,1,0,null,0]  → "abc"
 *
 *  CONSTRAINTS:  1 <= nodes <= 8500;  0 <= val <= 25.
 *
 *  HINTS
 *  -----
 *   1. DFS pushing chars onto a buffer.
 *   2. At a leaf, reverse the buffer to obtain leaf-to-root string and compare.
 * ============================================================ */
public class SmallestFromLeaf {

    private String smallestSoFar = null;

    public String smallestFromLeaf(final TreeNode root) {
        depthFirst(root, new StringBuilder());
        return smallestSoFar;
    }

    private void depthFirst(final TreeNode node, final StringBuilder pathBuffer) {
        if (node == null) {
            return;
        }
        pathBuffer.append((char) ('a' + node.val));

        final boolean isLeaf = (node.left == null && node.right == null);
        if (isLeaf) {
            final String candidate = pathBuffer.reverse().toString();
            if (smallestSoFar == null || candidate.compareTo(smallestSoFar) < 0) {
                smallestSoFar = candidate;
            }
            pathBuffer.reverse();
        }

        depthFirst(node.left,  pathBuffer);
        depthFirst(node.right, pathBuffer);

        pathBuffer.deleteCharAt(pathBuffer.length() - 1);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  DFS maintaining root-to-current path in a StringBuilder. At each leaf,
 *  reverse the buffer to get leaf-to-root string and compare to running best.
 *
 *  Complexity: Time O(n * h) due to leaf string compares.
 *  Pattern: tree DFS with reversible path buffer.
 * ============================================================ */
