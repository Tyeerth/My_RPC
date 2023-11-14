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
        RpcRequestTransport nettyRpcClient = new NettyRpcClient();//实际的执行对象
        RpcClientProxy rpcClientProxy = new RpcClientProxy(nettyRpcClient);//里面封装一些代码执行的额外逻辑
        HelloService helloServiceProxy = rpcClientProxy.getProxy(HelloService.class);//代理对象
        String sendData = helloServiceProxy.hello(new Hello("111", "send data"));
        System.out.println(sendData);
    }
}
