package com.company.DSA.bst;

import com.company.DSA.common.TreeNode;
import java.util.*;

/* ============================================================
 *  LeetCode #501 — Find Mode in BST
 * ============================================================
 *  PROBLEM
 *  -------
 *  Return the mode(s) — the most frequent value(s) in a BST with duplicates.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,null,2,2] → [2]
 *  Ex2: [0] → [0]
 *  Ex3: [1,null,2,null,3,null,4] → [1,2,3,4]
 *
 *  CONSTRAINTS:  1 <= nodes <= 10^4;  -10^5 <= val <= 10^5.
 *
 *  HINTS
 *  -----
 *   1. Inorder yields sorted with duplicates contiguous.
 *   2. Track run length; update max + modes list.
 * ============================================================ */
public class FindModeInBST {

    private Integer previousValue;
    private int currentRunLength = 0;
    private int maxRunLength     = 0;
    private final List<Integer> modes = new ArrayList<>();

    public int[] findMode(final TreeNode root) {
        inorder(root);

        final int[] result = new int[modes.size()];
        for (int index = 0; index < result.length; index++) {
            result[index] = modes.get(index);
        }
        return result;
    }

    private void inorder(final TreeNode node) {
        if (node == null) {
            return;
        }
        inorder(node.left);

        currentRunLength = (previousValue != null && previousValue == node.val)
                ? currentRunLength + 1
                : 1;

        if (currentRunLength > maxRunLength) {
            maxRunLength = currentRunLength;
            modes.clear();
            modes.add(node.val);
        } else if (currentRunLength == maxRunLength) {
            modes.add(node.val);
        }

        previousValue = node.val;
        inorder(node.right);
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  Inorder traversal: equal duplicate values are adjacent. Counter increments
 *  on equal, resets to 1 on new value. New max → clear and add; tie → append.
 *
 *  Complexity: Time O(n), Space O(h) recursion + O(modes) output.
 *  Pattern: streamed-mode detection over a sorted sequence.
 * ============================================================ */
