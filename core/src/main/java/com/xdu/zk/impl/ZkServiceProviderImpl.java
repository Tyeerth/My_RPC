package com.xdu.zk.impl;

import com.xdu.Enum.RpcErrorMessageEnum;
import com.xdu.Exception.RpcException;
import com.xdu.config.RpcServiceConfig;
import com.xdu.server.NettyServer;
import com.xdu.zk.ServiceProvider;
import com.xdu.zk.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tyeerth
 * @date 2023/11/14 - 上午11:13
 * @description
 */
@Slf4j
public class ZkServiceProviderImpl implements ServiceProvider {
    private final Set<String> registeredServices;
    private Map<String,Object> serviceMap;
    private final ServiceRegistry serviceRegistry;

    public ZkServiceProviderImpl(){
        registeredServices = ConcurrentHashMap.newKeySet();
        serviceMap = new ConcurrentHashMap<String,Object>();
        serviceRegistry = new ZkServiceRegistryImpl();
    }
    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {
        String rpcSerivceName = rpcServiceConfig.getRpcServiceName();
        if (registeredServices.contains(rpcSerivceName)){
            return;
        }
        registeredServices.add(rpcSerivceName);
        serviceMap.put(rpcSerivceName,rpcServiceConfig.getService());                             //获得对象所实现的接口
        log.info("Added service :{} and interface :{}",rpcSerivceName,rpcServiceConfig.getService().getClass().getInterfaces());
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (null == service) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            this.addService(rpcServiceConfig);
            serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(), new InetSocketAddress(host, NettyServer.PORT));
        } catch (UnknownHostException e) {
            log.error("occur exception when getHostAddress", e);
        }
    }
}
