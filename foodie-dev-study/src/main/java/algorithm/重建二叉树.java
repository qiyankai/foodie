package algorithm;

//输入某二叉树的前序遍历和中序遍历的结果，请重建该二叉树。假设输入的前序遍历和中序遍历的结果中都不含重复的数字。
//
// 
//
// 例如，给出 
//
// 前序遍历 preorder = [3,9,20,15,7]
//中序遍历 inorder = [9,3,15,20,7] 
//
// 返回如下的二叉树： 
//
//     3
//   / \
//  9  20
//    /  \
//   15   7 
//
// 
//
// 限制： 
//
// 0 <= 节点个数 <= 5000 
//
// 
//
// 注意：本题与主站 105 题重复：https://leetcode-cn.com/problems/construct-binary-tree-from-
//preorder-and-inorder-traversal/ 
// Related Topics 树 递归


//leetcode submit region begin(Prohibit modification and deletion)


class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int x) {
        val = x;
    }
}

public class 重建二叉树 {
    public static void main(String[] args) {
        int[] preorder = {3, 1, 2, 4};
        int[] inorder = {1, 2, 3, 4};

        buildTree(preorder, inorder);
    }

    public static TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder == null || preorder.length == 0) {
            return null;
        }
        // 找到其在中序遍历的位置，左边的都是左子树，右边的是右子树
        int size = preorder.length;
        TreeNode headNode = buildTree(preorder, 0, inorder, 0, size);
        return headNode;
    }

    //     前序遍历 preorder = [3,9,20,15,7]
//中序遍历 inorder =       [9,3,15,20,7]
//                        |----------|   size
//                        |--4-----|     index[3]
//                            |--3---|   length
//                            |1|        left
//                                 |1|   right
//
//  [3,1,2,4]
//  [1,2,3,4]
//     1(preL
//     0(inorL
//     2(size

//     3(preL
//     3(inorL
//     1(size

    public static TreeNode buildTree(int[] preorder, int preL, int[] inorder, int inorL, int size) {
        // 判断此时是否下标越界，或者当前位置无子树
        if (size == 0 || preL == preorder.length) {
            return null;
        }
        // 把当前前序值取出，为此树ROOT节点
        int rootVal = preorder[preL];
        TreeNode currNode = new TreeNode(rootVal);
        if (size == 1) {
            return currNode;
        }
        // 在中序中找到刚才前序的ROOT位置，确定左子树长度，右子树长度，由此可以确定一颗树
        int rootIndex;
        for (rootIndex = 0; rootIndex < inorder.length; rootIndex++) {
            if (inorder[rootIndex] == rootVal)
                break;
        }

        // 左子树，前序从当前位置+1开始算，1为当前节点本体
        int leftL = preL + 1;
        int leftLength = rootIndex - inorL;

        // 右子树，前序从左子树左边界+左子树长度
        int rightL = leftL + leftLength;
        int rightLength = size-leftLength-1;

        TreeNode leftNode = buildTree(preorder, leftL, inorder, inorL, leftLength);
        TreeNode rightNode = buildTree(preorder, rightL, inorder, rootIndex + 1, rightLength);

        currNode.left = leftNode;
        currNode.right = rightNode;
        return currNode;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
