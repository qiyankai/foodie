package algorithm.二叉树;

import java.util.LinkedList;
import java.util.List;

public class 二叉树展开为链表 {
    public void flatten(TreeNode root) {
        preorderTraversal(root);
    }

    public TreeNode preorderTraversal(TreeNode node) {
        if (node == null) {
            return null;
        }
        TreeNode leftNode = preorderTraversal(node.left);
        TreeNode rightNode = preorderTraversal(node.right);
        // 把右节点挂在左节点最后位置
        if (leftNode!=null) {
            while (leftNode.right != null) {
                leftNode = leftNode.right;
            }
            leftNode.right = rightNode;
            node.right = leftNode;
            node.left = null;
        }
        return node;
    }
}
