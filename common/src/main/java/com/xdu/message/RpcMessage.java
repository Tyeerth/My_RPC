package com.xdu.message;

import lombok.*;

/**
 * @Author tyeerth
 * @Date 2023/10/26 10:53
 * @Description
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder //使用builder注解使得方法可以连续赋值
@ToString
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
