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

    public static final byte REQUEST_TYPE = 1;
    public static final byte RESPONSE_TYPE = 2;
    public static final byte HEARTBEAT_REQUEST_TYPE = 3;
    //pong
    public static final byte HEARTBEAT_RESPONSE_TYPE = 4;
    public static final int HEAD_LENGTH = 16;

    public static final String PING = "ping";
    public static final String PONG = "pong";
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;

}
