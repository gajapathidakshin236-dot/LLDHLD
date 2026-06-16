package com.company.DSA;

import java.util.*;

/**
 * LeetCode #501 - Find Mode in BST (may have dups)
 * Inorder gives sorted; track current streak, max streak, modes list.
 * Time: O(n)  Space: O(h)
 */
public class FindModeInBST {
    Integer prev = null;
    int cnt = 0, maxCnt = 0;
    List<Integer> modes = new ArrayList<>();
    public int[] findMode(TreeNode root) {
        in(root);
        int[] res = new int[modes.size()];
        for (int i = 0; i < res.length; i++) res[i] = modes.get(i);
        return res;
    }
    private void in(TreeNode n) {
        if (n == null) return;
        in(n.left);
        if (prev != null && prev == n.val) cnt++; else cnt = 1;
        if (cnt > maxCnt) { maxCnt = cnt; modes.clear(); modes.add(n.val); }
        else if (cnt == maxCnt) modes.add(n.val);
        prev = n.val;
        in(n.right);
    }
}
