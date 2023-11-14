package com.xdu.zk.impl;

import com.xdu.config.RpcServiceConfig;
import com.xdu.zk.ServiceProvider;
import lombok.extern.slf4j.Slf4j;

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
    public ZkServiceProviderImpl(){
        registeredServices = ConcurrentHashMap.newKeySet();
        serviceMap = new ConcurrentHashMap<String,Object>();
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
        return null;
    }

    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {

    }
}
