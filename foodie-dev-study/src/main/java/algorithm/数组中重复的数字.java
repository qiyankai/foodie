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
}
class Solution {
    public int findRepeatNumber(int[] nums) {
        if (nums != null && nums.length != 0) {
            Set<Integer> set = new HashSet<Integer>();
            for (int i = 0; i < nums.length; i++) {
                set.add(nums[i]);
                if (set.size() != (i + 1)){
                    return nums[i];
                }
            }
        }
        return 0;
    }
}