package com.company.DSA;

import java.util.*;

/**
 * LeetCode #106 - Construct Binary Tree from Inorder and Postorder
 * Postorder's last is root; find in inorder map; recurse right then left (postorder is L,R,root).
 * Time: O(n)  Space: O(n)
 */
public class BuildTreeFromInPost {
    int post;
    Map<Integer,Integer> idx = new HashMap<>();
    int[] postorder;
    public TreeNode buildTree(int[] inorder, int[] postorder) {
        this.postorder = postorder;
        post = postorder.length - 1;
        for (int i = 0; i < inorder.length; i++) idx.put(inorder[i], i);
        return build(0, inorder.length - 1);
    }
    private TreeNode build(int l, int r) {
        if (l > r) return null;
        int v = postorder[post--];
        TreeNode n = new TreeNode(v);
        int mid = idx.get(v);
        n.right = build(mid + 1, r);
        n.left  = build(l, mid - 1);
        return n;
    }
}
