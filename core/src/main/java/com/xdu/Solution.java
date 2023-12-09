package com.xdu;

import java.util.*;

class Solution {
    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> hashMap = new HashMap<>();
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            List<String> stringList = hashMap.getOrDefault(key, new ArrayList<>());
            stringList.add(str);
            hashMap.put(key, stringList);
        }
        System.out.println(hashMap.values());
        return new ArrayList<List<String>>(hashMap.values());
    }

    public static void main(String[] args) {
        String[] strs = {"eat","tea","tan","ate","nat","bat"};
        System.out.println(new ArrayList<>(1));
    }
}