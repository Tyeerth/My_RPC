package com.xdu.transport;

import com.xdu.message.RpcRequest;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @author tyeerth
 * @date 2023/11/7 - 下午3:56
 * @description
 */
public interface RpcRequestTransport {
    Object sendRpcRequest(RpcRequest rpcRequest);
}
