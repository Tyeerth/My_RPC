package com.xdu.message;

import lombok.Data;

/**
 * @Author tyeerth
 * @Date 2023/10/26 10:53
 * @Description
 */
@Data
public class RpcMessage {
    /**
     * Rpc message type
     */
    private byte messageType;
    /**
     * serial type
     */
    private byte codec;
    /**
     * compress type
     */
    private byte compress;
    /**
     * request id
     *
     */
    private int requestId;
    /**
     * data
     */
    private Object data;

}
