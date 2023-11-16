package com.xdu.client.handler;

import com.xdu.factory.SingleFactory;
import com.xdu.message.RpcRequest;
import com.xdu.zk.ServiceProvider;
import com.xdu.zk.impl.ZkServiceProviderImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author tyeerth
 * @date 2023/11/15 - 下午3:30
 * @description
 */
public class RpcRequstHandler {
    private final ServiceProvider serviceProvider;

    public RpcRequstHandler(){
        serviceProvider = SingleFactory.getInstance(ZkServiceProviderImpl.class);
    }
    public Object invokeTargetMethod(RpcRequest rpcRequest) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        Object service = serviceProvider.getService(rpcServiceName);//获得实际对象
        Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        Object invoke = method.invoke(service, rpcRequest.getParameters());
        return invoke;
    }

}
