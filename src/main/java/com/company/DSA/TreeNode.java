package com.company.DSA;

import java.util.*;

/** Standard tree node used across DSA tree solutions. */
public class TreeNode {
    public int val;
    public TreeNode left, right;
    public TreeNode() {}
    public TreeNode(int v) { this.val = v; }
    public TreeNode(int v, TreeNode l, TreeNode r) { this.val = v; this.left = l; this.right = r; }
}
