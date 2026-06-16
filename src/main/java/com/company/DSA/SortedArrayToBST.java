package com.company.DSA;

/**
 * LeetCode #108 - Convert Sorted Array to BST
 * Pick mid as root; recurse left/right halves.
 * Time: O(n)  Space: O(log n)
 */
public class SortedArrayToBST {
    public TreeNode sortedArrayToBST(int[] nums) { return build(nums, 0, nums.length - 1); }
    private TreeNode build(int[] a, int l, int r) {
        if (l > r) return null;
        int m = l + (r - l) / 2;
        TreeNode n = new TreeNode(a[m]);
        n.left  = build(a, l, m - 1);
        n.right = build(a, m + 1, r);
        return n;
    }
}
