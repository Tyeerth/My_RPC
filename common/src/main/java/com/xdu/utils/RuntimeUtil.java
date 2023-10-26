package com.xdu.utils;

/**
 * @Author tyeerth
 * @Date 2023/10/26 21:27
 * @Description
 */
public class RuntimeUtil {
    /**
     * 获取CPU的核心数
     *
     * @return cpu的核心数
     */
    public static int cpus() {
        return Runtime.getRuntime().availableProcessors();
    }
}
