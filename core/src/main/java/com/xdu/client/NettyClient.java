package com.xdu.client;

import com.xdu.proxy.RpcClientProxy;
import com.xdu.service.Hello;
import com.xdu.service.HelloService;
import com.xdu.transport.RpcRequestTransport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author tyeerth
 * @date 2023/11/2 - 上午10:58
 * @description
 */
@Slf4j
@Component
public class NettyClient {
    public static void main(String[] args)  {
        // initing netty
        RpcRequestTransport nettyRpcClient = new NettyRpcClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(nettyRpcClient);
        HelloService helloServiceProxy = rpcClientProxy.getProxy(HelloService.class);
        String sendData = helloServiceProxy.hello(new Hello("111", "send data"));
        System.out.println(sendData);
    }
}
