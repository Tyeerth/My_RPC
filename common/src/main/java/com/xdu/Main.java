package com.xdu;

import java.util.Arrays;

class Main {
    public static int maxArea(int h, int w, int[] horizontalCuts, int[] verticalCuts) {
        Arrays.sort(horizontalCuts);
        Arrays.sort(verticalCuts);
        int horizontal_length = horizontalCuts.length;
        int vertical_length = verticalCuts.length;
        // 取相邻最大差值,记得要统计上边界
        int horizontal_max = horizontalCuts[0];
        int vertical_max = verticalCuts[0];
        for (int i = 1; i < horizontal_length; i++) {
            horizontal_max = Math.max(horizontal_max, horizontalCuts[i] - horizontalCuts[i - 1]);
        }
        horizontal_max = Math.max(horizontal_max, h - horizontalCuts[horizontal_length - 1]);
        for (int i = 1; i < horizontal_length; i++) {
            vertical_max = Math.max(vertical_max, verticalCuts[i] - verticalCuts[i - 1]);
        }
        vertical_max = Math.max(vertical_max, w - verticalCuts[vertical_length - 1]);
        return horizontal_max * vertical_max % (1000000007);
    }

    public static void main(String[] args) {
        int h = 5, w = 4;
        int[] horizontalCuts = {3,1};
        int[] verticalCuts = {1};
        System.out.println(maxArea(h, w, horizontalCuts, verticalCuts));
    }
}