package com.xdu.proxy;

import com.xdu.Enum.RpcErrorMessageEnum;
import com.xdu.Enum.RpcResponseCodeEnum;
import com.xdu.Exception.RpcException;
import com.xdu.client.NettyRpcClient;
import com.xdu.message.RpcRequest;
import com.xdu.message.RpcResponse;
import com.xdu.transport.RpcRequestTransport;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author tyeerth
 * @date 2023/11/3 - 下午4:05
 * @description Dynamic proxy class
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {

    private static final String INTERFACE_NAME = "interfaceName";

    /**
     *  The way used to requests to the server .And there are two implements: socket and netty
     */
    private final RpcRequestTransport requestTransport;
    public RpcClientProxy(RpcRequestTransport rpcRequestTransport){
        this.requestTransport =rpcRequestTransport;
    }
    @Override//代理对象本身、被调用的方法对象和方法的参数数组
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        log.info("invoked method {}",method.getName());
        RpcRequest rpcRequest = RpcRequest.builder().methodName(method.getName())
                .parameters(args)
                .group("test")
                .version("version2")
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .build();
        RpcResponse<Object> rpcResponse = null;
        log.info("send data :"+rpcRequest);
        //TODO 通过接口的实例化对象来判断对象属于什么类型
        if (requestTransport instanceof NettyRpcClient){
            CompletableFuture<RpcResponse<Object>> completableFuture = (CompletableFuture<RpcResponse<Object>>) requestTransport.sendRpcRequest(rpcRequest);
            rpcResponse = completableFuture.get();
        }
        this.check(rpcResponse, rpcRequest);
        log.info("receiver data :"+rpcResponse.getData());
        return rpcResponse.getData();
    }
    private void check(RpcResponse<Object> rpcResponse, RpcRequest rpcRequest) {
        if (rpcResponse == null) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcErrorMessageEnum.REQUEST_NOT_MATCH_RESPONSE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (rpcResponse.getCode() == null || !rpcResponse.getCode().equals(RpcResponseCodeEnum.SUCCESS.getCode())) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }


    public <T> T getProxy(Class<T> clazz){
        //在这行代码中，this 关键字指向当前类的实例。当前类实现了 InvocationHandler 接口，并且该实例会作为 invoke() 方法的实现。代理对象是requestTransport

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},this);
    }
}
