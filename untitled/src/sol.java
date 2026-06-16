import java.util.concurrent.atomic.AtomicInteger;

class Solution {

    public boolean isValidBST(tree.TreeNode root) {
        return dfs(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean dfs(tree.TreeNode node, long low, long high) {
        if (node == null) return true;

        if (node.val <= low || node.val >= high) {
            return false;
        }


        return dfs(node.left, low, node.val)
                && dfs(node.right, node.val, high);
    }

    int max = 0;

    public int diameterOfBinaryTree(tree.TreeNode root) {
        dfs(root);
        return max;
    }

    private int dfs(tree.TreeNode node) {

        if (node == null) {
            return 0;
        }

        int left = dfs(node.left);
        int right = dfs(node.right);

        max = Math.max(max, left + right);

        return 1 + Math.max(left, right);
    }



    public boolean isBalanced(tree.TreeNode root) {
        max=0;
        return checkHeight(root) != -1;
    }

    private int checkHeight(tree.TreeNode node) {
        if (node == null) {
            return 0;
        }

        int left = dfs(node.left);
        int right = dfs(node.right);
        if (left == -1 || right == -1) {
            return -1;
        }

        if (Math.abs(left - right) > 1) {
            return -1;
        }

        return 1 + Math.max(left, right);
    }


}