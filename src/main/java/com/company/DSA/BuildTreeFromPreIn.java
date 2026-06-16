package com.company.DSA;

import java.util.*;

/**
 * LeetCode #105 - Construct Binary Tree from Preorder and Inorder Traversal
 * preorder[0] is root; find it in inorder to split L/R subtrees. Use map for O(1) lookup.
 * Time: O(n)  Space: O(n)
 */
public class BuildTreeFromPreIn {
    int pre = 0;
    Map<Integer,Integer> idx = new HashMap<>();
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        for (int i = 0; i < inorder.length; i++) idx.put(inorder[i], i);
        return build(preorder, 0, inorder.length - 1);
    }
    private TreeNode build(int[] preorder, int l, int r) {
        if (l > r) return null;
        int v = preorder[pre++];
        TreeNode n = new TreeNode(v);
        int mid = idx.get(v);
        n.left  = build(preorder, l, mid - 1);
        n.right = build(preorder, mid + 1, r);
        return n;
    }
}
