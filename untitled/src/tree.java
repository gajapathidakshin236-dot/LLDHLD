import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class tree {

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {}

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public  static List<Integer>  bfs(TreeNode root){

    Queue<TreeNode> queue = new LinkedList<>();
     queue.offer(root);
     List<Integer> res= new ArrayList<>();

     while (!queue.isEmpty()) {

         int size = queue.size();

         int righy=-1;

         for (int i = 0; i < size; i++) {

             TreeNode node = queue.poll();
             righy =node.val;

             if (node.left != null) {
                 queue.offer(node.left);

             }

             if (node.right != null) {
                 queue.offer(node.right);
             }
         }
         res.add(righy);
     }
     return res;
    }

    class Solution {

        public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
            return dfs(root, p, q);
        }

        private static  TreeNode dfs(TreeNode root, TreeNode p, TreeNode q) {

            if (root == null) {
                return null;
            }

            if (root == p || root == q) {
                return root;
            }

            TreeNode left = dfs(root.left, p, q);
            TreeNode right = dfs(root.right, p, q);

            // p and q found on different sides
            if (left != null && right != null) {
                return root;
            }

            // propagate non-null result upward
            if (left != null) {
                return left;
            }

            return right;
        }
    }

    public static void main(String[] args) {
        TreeNode TreeNode=new TreeNode(12,null,null);
        Solution.dfs(TreeNode,TreeNode,TreeNode);
    }




}


