package com.xdu;

import com.xdu.server.annotation.RpcScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author tyeerth
 * @date 2023/11/2 - 上午10:43
 * @description
 */
@RpcScan(ScanPackage = {})
public class NettyClientMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyClientMain.class);
        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
        helloController.test();
    }
}
