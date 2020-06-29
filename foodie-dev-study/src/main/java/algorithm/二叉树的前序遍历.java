package algorithm;

import java.util.ArrayList;
import java.util.List;

public class 二叉树的前序遍历 {
    public List<Integer> preorderTraversal(TreeNode root) {
        ArrayList<Integer> resultList = new ArrayList<>();
        preorderTraversal(root, resultList);
        return resultList;
    }

    public void preorderTraversal(TreeNode node, List<Integer> preList) {
        if (node == null) {
            return;
        }
        preorderTraversal(node.left, preList);
        preList.add(node.val);
        preorderTraversal(node.right, preList);
    }

    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> resultList = new ArrayList<>();
        return resultList;
    }

    public void levelOrder(TreeNode node, List<List<Integer>> preList) {
        if (node == null) {
            return;
        }
        levelOrder(node.left, preList);
        levelOrder(node.right, preList);
    }
}
