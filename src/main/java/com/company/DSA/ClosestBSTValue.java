package com.company.DSA;

/**
 * LeetCode #270 - Closest Binary Search Tree Value
 * Walk down BST tracking closest; go left/right based on comparison.
 * Time: O(h)  Space: O(1)
 */
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
