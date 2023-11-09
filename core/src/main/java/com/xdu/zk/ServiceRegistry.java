package com.xdu.zk.util;

import java.net.InetSocketAddress;

/**
 * @author tyeerth
 * @date 2023/11/9 - 下午4:24
 * @description
 */
public interface ServiceRegistry {
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}
