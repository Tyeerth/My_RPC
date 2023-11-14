package com.xdu.zk;

import com.xdu.config.RpcServiceConfig;

/**
 * @author tyeerth
 * @date 2023/11/9 - 下午5:04
 * @description provider services for consumer
 */
public interface ServiceProvider {
    /**
     * 使用RpcServiceConfig是因为接口可能有多个实现类，需要指定分组和version来确定不同的实现类
     * @param rpcServiceConfig
     */
    void addService(RpcServiceConfig rpcServiceConfig);

    Object getService(String rpcServiceName);

    void publishService(RpcServiceConfig rpcServiceConfig);
}
