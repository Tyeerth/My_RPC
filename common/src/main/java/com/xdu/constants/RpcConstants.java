package com.xdu.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Author tyeerth
 * @Date 2023/10/26 11:04
 * @Description
 */
public class RpcConstants {
    /**
     * Magic number,Verify RpcMessage
     */
    public static final byte[] MAGIC_NUMBER = {'g','r','p','c'};

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static final byte VERSION = 1;

    public static final byte TOTAL_LENGTH = 16;



}
