package com.xdu.proxy.proxy_demo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JdkProxy implements InvocationHandler {
	
	//这里引入一个对象target，和他建立关联，后续对他的方法实现增强。
	//当然如果后面invoke方法的实现与该对象无关，此处可以不引入。
    private Object target;

    public JdkProxy(Object target) {
        this.target = target;
    }
    
    /**
     * @param proxy 代理对象
     * @param method 代理方法
     * @param args 方法参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("这里是代理方法");
        //这里所做的增强，实际只是输出一行文案，后面依旧是调用了目标对象的方法。
        return method.invoke(target, args);
    }
}
