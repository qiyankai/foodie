package algorithm.回溯算法;

import java.util.*;

public class RestoreIpAddresses {
    public static void main(String[] args) {
        restoreIpAddresses("25525511135").forEach(System.out::println);
    }

    public static List<String> restoreIpAddresses(String s) {
        List<String> res = new ArrayList<>();
        Deque<String> pathDeque = new ArrayDeque<>(4);
        // 判断是否有超过最大情况、最小情况
        int length = s.length();
        if (length >= 4 && length <= 12) {
            // 深度优先遍历
            dfs(s, 0, length, 4, pathDeque, res);
        }
        return res;
    }

    public static void dfs(String str, int start, int len, int ceng, Deque<String> pathDeque, List<String> res) {
        // 判断是否到达最后一次遍历
        if (len == start) {
            if (ceng == 0) {
                res.add(String.join(".", pathDeque));
            }
        }

        // 判断当前截取后，剩余部分是否符合最大、最小标准
        int residue = len - start;
        if (residue < ceng || residue > 3 * ceng) return;

        // 生成不同分支
        for (int i = 0; i < 3; i++) {
            if (start + i >= len) {
                return;
            }
            int ipSegment = judgeIfIpSegment(str, start, start + i);
            if (ipSegment != -1) {
                pathDeque.addLast(ipSegment + "");
                dfs(str, start + i + 1, len, ceng - 1, pathDeque, res);
            }
        }
    }

    /**
     * 判断 s 的子区间 [left, right] 是否能够成为一个 ip 段
     * 判断的同时顺便把类型转了
     *
     * @param s
     * @param left
     * @param right
     * @return
     */
    public static int judgeIfIpSegment(String s, int left, int right) {
        int len = right - left + 1;

        // 大于 1 位的时候，不能以 0 开头
        if (len > 1 && s.charAt(left) == '0') {
            return -1;
        }

        // 转成 int 类型
        int res = 0;
        for (int i = left; i <= right; i++) {
            res = res * 10 + s.charAt(i) - '0';
        }

        if (res > 255) {
            return -1;
        }
        return res;
    }

}