package com.xdu;

import com.xdu.annotation.RpcScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author tyeerth
 * @Date 2023/10/25 21:25
 * @Description
 */
@RpcScan(ScanPackage = {"com.xdu"})
public class NettyServerMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyServerMain.class);
        //根据bean的名字获得与之对应的实例(从IOC容器中获取）
        applicationContext.getBean("nettyRpcServer");
    }
}
