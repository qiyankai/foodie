package algorithm.二叉树;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class 二叉树的层序遍历 {
    public List<List<Integer>> levelOrder(TreeNode root) {

        List<List<Integer>> resultList = new ArrayList<>();
        if (root == null) {
            return resultList;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            List<Integer> currLevel = new ArrayList<Integer>();
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode currLevelNode = queue.poll();
                currLevel.add(currLevelNode.val);
                if (currLevelNode.left != null) queue.offer(currLevelNode.left);
                if (currLevelNode.right != null) queue.offer(currLevelNode.right);
            }
            resultList.add(currLevel);
        }
        return resultList;
    }
}