package com.xdu;


import java.util.*;
import java.util.stream.Collectors;
class Solution {

}

public class Main {
    public static int findTheLongestBalancedSubstring(String s) {
        int res = 0;
        int n = s.length();
        int[] count = new int[2];
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '1') {
                count[1]++;
                res = Math.max(res, 2 * Math.min(count[0], count[1]));
            } else {
                if (i == 0 || s.charAt(i -1) == '1'){
                    count[0] = 1;
                }else {
                    count[0]++;
                }
                count[1] = 0;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(findTheLongestBalancedSubstring("011"));
//        System.out.println(comput_number("101", 0));
    }
}