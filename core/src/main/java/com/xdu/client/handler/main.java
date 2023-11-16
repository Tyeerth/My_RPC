package com.xdu.client.handler;

/**
 * @author tyeerth
 * @date 2023/11/16 - 上午10:23
 * @description
 */
public class main {
    public static int longestAlternatingSubarray(int[] nums, int threshold) {
        int result = 0;
        int temp = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] % 2 == 0 && nums[i] <= threshold) {
                temp++;
                //judge the next number
                int k = i + 1;
                while (k < nums.length) {
                    if (nums[k] % 2 != nums[k - 1] % 2 && nums[k] <= threshold) {
                        temp++;
                        k++;
                    } else {
                        break;
                    }
                }
                result = Math.max(result, temp);
                temp = 0;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int[] nums = {2,3,4,5};
        System.out.println(longestAlternatingSubarray(nums,4));
    }
}
