package com.company.DSA;

import java.util.*;

/**
 * LeetCode #230 - Kth Smallest in BST
 * Iterative inorder using stack; pop k-th element.
 * Time: O(h+k)  Space: O(h)
 */
public class KthSmallestBST {
    public int kthSmallest(TreeNode root, int k) {
        Deque<TreeNode> st = new ArrayDeque<>();
        TreeNode cur = root;
        while (cur != null || !st.isEmpty()) {
            while (cur != null) { st.push(cur); cur = cur.left; }
            cur = st.pop();
            if (--k == 0) return cur.val;
            cur = cur.right;
        }
        return -1;
    }
}
