package com.xdu;

import com.xdu.server.annotation.RpcReference;
import com.xdu.service.Hello;
import com.xdu.service.HelloService;
import org.springframework.stereotype.Component;

/**
 * @author tyeerth
 * @date 2023/12/4 - 下午8:25
 * @description
 */
@Component
public class HelloController {
    @RpcReference(version = "version001",group = "test001")
    private HelloService helloService;

    public void test(){
        String hello = helloService.hello(new Hello("111", "222"));
        assert "Hello description is 222".equals(hello);
        for (int i = 0; i < 10; i++) {
            System.out.println(helloService.hello(new Hello("111", "222")));
        }
    }

}
