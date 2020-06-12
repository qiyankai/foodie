package algorithm;

import java.util.HashSet;
import java.util.Set;

public class 数组中重复的数字 {
    public static void main(String[] args) {

    }

    // 暴力解法
    public int findRepeatNumber(int[] nums) {

        if (nums != null && nums.length != 0) {
            for (int i = 0; i < nums.length - 1; i++) {
                for (int j = i + 1; j < nums.length - 1; j++) {
                    if (nums[j] == nums[i]) {
                        return nums[j];
                    }
                }
            }
        }
        return 0;
    }
    /**
     * 由于只需要找出数组中任意一个重复的数字，因此遍历数组，遇到重复的数字即返回。为了判断一个数字是否重复遇到，使用集合存储已经遇到的数字，如果遇到的一个数字已经在集合中，则当前的数字是重复数字。
     * <p>
     * 初始化集合为空集合，重复的数字 repeat = -1
     * 遍历数组中的每个元素：
     * 将该元素加入集合中，判断是否添加成功
     * 如果添加失败，说明该元素已经在集合中，因此该元素是重复元素，将该元素的值赋给 repeat，并结束遍历
     *
     * @param nums
     * @return
     */
    public int Set集合存储(int[] nums) {
        if (nums != null && nums.length != 0) {
            Set<Integer> set = new HashSet<Integer>();
            for (int i = 0; i < nums.length; i++) {
                set.add(nums[i]);
                if (set.size() != (i + 1)) {
                    return nums[i];
                }
            }
        }
        return 0;
    }

    /**
     * 原地置换
     * 如果没有重复数字，那么正常排序后，数字i应该在下标为i的位置
     * 所以思路是重头扫描数组，遇到下标为i的数字如果不是i的话，（假设为m),那么我们就拿与下标m的数字交换。
     * 在交换过程中，发现这个数字重复，那么终止返回-1
     *
     * @param nums
     * @return
     */
    public int 原地置换(int[] nums) {
        int temp;
        for (int i = 0; i < nums.length; i++) {
            while (nums[i] != i) {
                if (nums[i] == nums[nums[i]]) {
                    return nums[i];
                }
                temp = nums[i];
                nums[i] = nums[temp];
                nums[temp] = temp;
            }
        }
        return -1;
    }

}

