package com.company.DSA;

/* ============================================================
 *  LeetCode #270 — Closest Binary Search Tree Value
 * ============================================================
 *  PROBLEM
 *  -------
 *  Given a BST and a target value (double), return the value in the BST that
 *  is closest to the target.
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
 *   2. Don't need to traverse the whole tree.
 * ============================================================ */
public class ClosestBSTValue {
    public int closestValue(TreeNode root, double target) {
        int closest = root.val;
        while (root != null) {
            if (Math.abs(root.val - target) < Math.abs(closest - target)) closest = root.val;
            root = target < root.val ? root.left : root.right;
        }
        return closest;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  BST-guided walk down. Compare each node's value to target; update closest if
 *  closer. Move left if target is smaller (smaller values lie left), right otherwise.
 *
 *  Complexity: Time O(h), Space O(1).
 *  Edge cases: target outside tree's range → returns closest extreme; equal value → exact.
 *  Pattern: BST-aware descent.
 * ============================================================ */
