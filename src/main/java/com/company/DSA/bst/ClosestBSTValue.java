package com.company.DSA.bst;

import com.company.DSA.common.TreeNode;

/* ============================================================
 *  LeetCode #270 — Closest Binary Search Tree Value
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the value in the BST that is closest to target (a double).
 *
 *  EXAMPLES
 *  --------
 *  Ex1: root=[4,2,5,1,3], target=3.714286 → 4
 *  Ex2: root=[1], target=4.428571 → 1
 *
 *  CONSTRAINTS:  1 <= nodes <= 10^4;  -10^9 <= val <= 10^9.
 *
 *  HINTS
 *  -----
 *   1. Walk down BST tracking closest. Compare and choose side based on value.
 * ============================================================ */
public class ClosestBSTValue {

    public int closestValue(TreeNode current, final double target) {
        int closestSoFar = current.val;

        while (current != null) {
            if (Math.abs(current.val - target) < Math.abs(closestSoFar - target)) {
                closestSoFar = current.val;
            }
            current = (target < current.val) ? current.left : current.right;
        }
        return closestSoFar;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  BST-guided walk: compare each node's value to target; update closest if
 *  closer. Move left if target is smaller, right otherwise.
 *
 *  Complexity: Time O(h), Space O(1).
 *  Pattern: BST-aware descent.
 * ============================================================ */
