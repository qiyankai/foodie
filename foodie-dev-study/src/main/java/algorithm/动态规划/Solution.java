package algorithm.动态规划;

import algorithm.二叉树.TreeNode;

class Solution {
    public int rob(TreeNode root) {
        if (root == null) return 0;
        TreeNode leftNode = root.left;
        TreeNode rightNode = root.right;
        if (leftNode == null && rightNode == null) return root.val;
        int erSum = rob(leftNode) + rob(rightNode);
        int sunSum = 0;
        if (rightNode != null) {
            sunSum += rob(rightNode.left) + rob(rightNode.right);
        }
        if (leftNode != null) {
            sunSum += rob(leftNode.left) + rob(leftNode.right);
        }
        return Math.max(erSum, root.val + sunSum);
    }

    public int rob2(TreeNode root) {
        int[] res = new int[101];
        int i = 0;
        while (root!=null){
            i++;
            TreeNode leftNode = root.left;
            TreeNode rightNode = root.right;
            if (leftNode == null && rightNode == null)
                res[i] = root.val;
            int erSum = rob(leftNode) + rob(rightNode);
            int sunSum = 0;
            if (rightNode != null) {
                sunSum += rob(rightNode.left) + rob(rightNode.right);
            }
            if (leftNode != null) {
                sunSum += rob(leftNode.left) + rob(leftNode.right);
            }
            return Math.max(erSum, root.val + sunSum);
        }
        return 0;
    }
}