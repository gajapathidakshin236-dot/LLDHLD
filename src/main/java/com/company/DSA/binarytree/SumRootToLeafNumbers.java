package com.company.DSA.binarytree;

import com.company.DSA.common.TreeNode;

/* ============================================================
 *  LeetCode #129 — Sum Root to Leaf Numbers
 * ============================================================
 *  PROBLEM
 *  -------
 *  Each root-to-leaf path forms a number (concatenated digits). Return the sum.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,2,3]      → 25     (12 + 13)
 *  Ex2: [4,9,0,5,1]  → 1026
 *  Ex3: [1]          → 1
 *
 *  CONSTRAINTS:  1 <= n <= 1000;  0 <= val <= 9; depth <= 10.
 *
 *  HINTS
 *  -----
 *   1. Carry the running number down the DFS: new = cur*10 + node.val.
 *   2. At a leaf, return the assembled number.
 * ============================================================ */
public class SumRootToLeafNumbers {

    public int sumNumbers(final TreeNode root) {
        return accumulate(root, 0);
    }

    private int accumulate(final TreeNode node, final int numberFromRootSoFar) {
        if (node == null) {
            return 0;
        }
        final int numberAtThisNode = numberFromRootSoFar * 10 + node.val;

        final boolean isLeaf = (node.left == null && node.right == null);
        if (isLeaf) {
            return numberAtThisNode;
        }
        return accumulate(node.left,  numberAtThisNode) +
               accumulate(node.right, numberAtThisNode);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  DFS carrying the partial number. Multiply by 10 shifts digits; add val
 *  appends the new digit. Leaves contribute their assembled number; nulls 0.
 *
 *  Complexity: Time O(n), Space O(h).
 *  Pattern: carry accumulated state down a tree.
 * ============================================================ */
