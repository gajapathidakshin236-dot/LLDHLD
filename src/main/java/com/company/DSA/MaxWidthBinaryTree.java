package com.company.DSA;

import java.util.*;

/* ============================================================
 *  LeetCode #662 — Maximum Width of Binary Tree
 * ============================================================
 *  PROBLEM
 *  -------
 *  Width of a level = #cells between left-most & right-most non-null nodes
 *  on that level, INCLUDING the null gaps that would exist in a full binary
 *  tree at that level. Return max width across all levels.
 *
 *  EXAMPLES
 *  --------
 *  Ex1: [1,3,2,5,3,null,9]      → 4
 *  Ex2: [1,3,2,5,null,null,9,6,null,7] → 8
 *  Ex3: [1,3,2,5]               → 2
 *
 *  CONSTRAINTS:  1 <= nodes <= 3000;  -100 <= val <= 100; depth <= 12 typical.
 *
 *  HINTS
 *  -----
 *   1. Label each node by position index as if tree were complete: root=1; left=2i, right=2i+1.
 *   2. Per level, width = lastIndex - firstIndex + 1.
 *   3. To avoid index blow-up at deep levels, RENORMALIZE indices each level (subtract first).
 * ============================================================ */
public class MaxWidthBinaryTree {
    public int widthOfBinaryTree(TreeNode root) {
        if (root == null) return 0;
        Deque<TreeNode> nq = new ArrayDeque<>();
        Deque<Long> iq = new ArrayDeque<>();
        nq.offer(root); iq.offer(1L);
        int best = 0;
        while (!nq.isEmpty()) {
            int sz = nq.size();
            long first = iq.peek(), last = first;
            for (int i = 0; i < sz; i++) {
                TreeNode n = nq.poll();
                long idx = iq.poll() - first; // renormalize
                last = idx + first;
                if (n.left  != null) { nq.offer(n.left);  iq.offer(2 * idx); }
                if (n.right != null) { nq.offer(n.right); iq.offer(2 * idx + 1); }
            }
            best = (int) Math.max(best, last - first + 1);
        }
        return best;
    }
}

/* ============================================================
 *  APPROACH
 * ============================================================
 *  BFS with positional labels: at depth d, indexing as in a complete BT gives
 *  each node a unique position. Width on that level is right_index - left_index + 1.
 *
 *  Renormalization:
 *    Indices can overflow Long at very deep trees. By subtracting the FIRST
 *    index of each level, we keep them small for the next level's children.
 *
 *  Complexity: Time O(n), Space O(n).
 *  Pattern: positional labeling on trees.
 * ============================================================ */
