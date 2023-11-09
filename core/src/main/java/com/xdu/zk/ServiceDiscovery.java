package com.xdu.zk;

import com.xdu.message.RpcRequest;

import java.net.InetSocketAddress;

/**
 * @author tyeerth
 * @date 2023/11/9 - 下午4:37
 * @description
 */
public interface ServiceDiscovery {
    InetSocketAddress lookupService(RpcRequest rpcRequest);
}
