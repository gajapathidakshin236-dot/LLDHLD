package com.company.DSA.binarytree;

import com.company.DSA.common.TreeNode;

import java.util.*;

public class tree {


    public static int depthMax(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int left = depthMax(root.left);
        int right = depthMax(root.right);

        return Math.max(left,right)+1;
    }

    /*1->2
    *  ->3
    * */

    public boolean isSameTree(TreeNode p, TreeNode q){
        if (p == null && q == null) {
            return true;
        }

        if (p == null || q == null) {
            return false;
        }
        if(p.val!=q.val){
            return false;
        }

        return isSameTree(p.left, q.left)
                && isSameTree(p.right, q.right);
    }

//nexttttttt

    public boolean isSymmetric(TreeNode root) {
        return mirror(root.left, root.right);
    }

    private boolean mirror(TreeNode left, TreeNode right) {

        if (left == null && right == null) {
            return true;
        }

        if (left == null || right == null) {
            return false;
        }

        if (left.val != right.val) {
            return false;
        }

        return mirror(left.left,right.right) && mirror(left.right ,right.left);
    }


    //nexttttttt

    public TreeNode invertTree(TreeNode root) {

        if (root == null) {
            return null;
        }
        TreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;
        invertTree(root.left);
        invertTree(root.right);
        return root;
    }


    public boolean hasPathSum(TreeNode root, int targetSum,int current) {
        boolean left=false,right=false;

        if (root == null) {
            return false;
        }

        if(root.left==null && root.right==null) {
            return current==targetSum;
        }

        if (root.left!=null) {
            left= hasPathSum(root.left,targetSum,current+root.left.val);
        }
        if (root.right!=null) {
           right =  hasPathSum(root.right,targetSum,current+root.right.val);
        }
        return left||right;
    }

    public boolean hasPathSum(TreeNode root, int targetSum) {

        if (root == null) {
            return false;
        }

        if (root.left == null && root.right == null) {
            return targetSum == root.val;
        }

        return hasPathSum(root.left, targetSum - root.val)
                || hasPathSum(root.right, targetSum - root.val);
    }

    public List<List<Integer>> levelOrder(TreeNode root) {

        List<List<Integer>> result = new ArrayList<>();

        if(root == null) {
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<>();

        queue.offer(root);

        while(!queue.isEmpty()) {

            int size = queue.size();

            List<Integer> level = new ArrayList<>();

            for(int i = 0; i < size; i++) {

                TreeNode current = queue.poll();

                level.add(current.val);

                if(current.left != null) {
                    queue.add(current.left);
                }

                if(current.right != null) {
                    queue.add(current.right);
                }
            }

            result.add(level);
        }

        return result;
    }

    public boolean isBalanced(TreeNode root) {
        return height(root) != -1;
    }

    private int height(TreeNode root) {

        if (root == null) {
            return 0;
        }

        int left = height(root.left);

        if (left == -1) {
            return -1;
        }

        int right = height(root.right);

        if (right == -1) {
            return -1;
        }

        if (Math.abs(left - right) > 1) {
            return -1;
        }

        return 1 + Math.max(left, right);
    }

int dia=0;

    public int hightDiam(TreeNode root) {

        if (root == null) {
            return 0;
        }

        int left = height(root.left);
        int right = height(root.right);

        dia= Math.max(dia,left+right);

        return 1 + Math.max(left, right);
    }
}
