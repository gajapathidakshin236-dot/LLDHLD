package com.company.DSA;

/**
 * LeetCode #988 - Smallest String Starting From Leaf
 * DFS with path StringBuilder; reverse path at leaf, compare to best.
 * Time: O(n*h)  Space: O(h)
 */
public class SmallestFromLeaf {
    String best = null;
    public String smallestFromLeaf(TreeNode root) {
        dfs(root, new StringBuilder());
        return best;
    }
    private void dfs(TreeNode n, StringBuilder sb) {
        if (n == null) return;
        sb.append((char) ('a' + n.val));
        if (n.left == null && n.right == null) {
            String s = sb.reverse().toString();
            if (best == null || s.compareTo(best) < 0) best = s;
            sb.reverse();
        }
        dfs(n.left, sb);
        dfs(n.right, sb);
        sb.deleteCharAt(sb.length() - 1);
    }
}
