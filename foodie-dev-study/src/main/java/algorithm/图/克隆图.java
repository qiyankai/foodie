package algorithm.图;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class 克隆图 {
    public Node cloneGraph(Node node) {
        Map<Node, Node> visited = new HashMap<>();
        // 如果当前visited有，取出返回
        if (visited.containsKey(node)) {
            return visited.get(node);
        }
        // 没有，创建新节点，塞进visited
        Node cloneNode = new Node(node.val, new ArrayList<>());
        visited.put(node, cloneNode);
        // 遍历递归调用邻居节点
        for (Node neighbor :
                node.neighbors) {
            cloneNode.neighbors.add(cloneGraph(neighbor));
        }
        return cloneNode;
    }
}

// Definition for a Node.
class Node {
    public int val;
    public List<Node> neighbors;

    public Node() {
        val = 0;
        neighbors = new ArrayList<Node>();
    }

    public Node(int _val) {
        val = _val;
        neighbors = new ArrayList<Node>();
    }

    public Node(int _val, ArrayList<Node> _neighbors) {
        val = _val;
        neighbors = _neighbors;
    }
}

