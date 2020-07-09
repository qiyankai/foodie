package algorithm;


public class 从头到尾打印链表 {
    public int[] reversePrint(ListNode head) {
        int len = 0;
        ListNode currNode = head;
        while (currNode != null) {
            len++;
            currNode = currNode.next;
        }
        int[] resultArr = new int[len];
        currNode = head;
        while (currNode != null) {
            resultArr[len--]=currNode.val;
            currNode = currNode.next;
        }
        return resultArr;
    }
}


class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
    }
}
