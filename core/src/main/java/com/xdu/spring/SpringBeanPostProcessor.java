package com.xdu.spring;

import com.xdu.client.NettyRpcClient;
import com.xdu.config.RpcServiceConfig;
import com.xdu.factory.SingleFactory;
import com.xdu.proxy.RpcClientProxy;
import com.xdu.server.annotation.RpcReference;
import com.xdu.server.annotation.RpcService;
import com.xdu.transport.RpcRequestTransport;
import com.xdu.zk.ServiceProvider;
import com.xdu.zk.impl.ZkServiceProviderImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author tyeerth
 * @date 2023/12/4 - 下午5:08
 * @description call this method before creating the bean to see if the class is annotated
 * postProcessBeforeInitialization bean初始化之前执行的方法
 * postProcessAfterInitialization bean初始化之后执行的方法
 */
@Slf4j
@Component
public class SpringBeanPostProcessor implements BeanPostProcessor {
    private final ServiceProvider serviceProvider;
    private final RpcRequestTransport rpcClient;
    public SpringBeanPostProcessor(){
        this.serviceProvider = SingleFactory.getInstance(ZkServiceProviderImpl.class);
        this.rpcClient = new NettyRpcClient();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //judge  if RpcService.class
        if (bean.getClass().isAnnotationPresent(RpcService.class)){
            log.info("[{}] is annotated with [{}]  before",bean.getClass(),RpcService.class.getCanonicalName());

            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);

            RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder().group(rpcService.group())
                    .version(rpcService.version())
                    .service(bean).build();
            serviceProvider.publishService(rpcServiceConfig);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //通过反射解析注解,获取类中的所有字段
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields){
            RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
            if (rpcReference != null){
                log.info("group:{},version:{}",rpcReference.group(),rpcReference.version());
                RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder().group(rpcReference.group())
                        .version(rpcReference.version()).build();
                RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient, rpcServiceConfig);
                Object clientProxy = rpcClientProxy.getProxy(declaredField.getType());

                declaredField.setAccessible(true);
                try {
                    declaredField.set(bean, clientProxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
