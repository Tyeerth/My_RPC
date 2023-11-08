package com.xdu.proxy.proxy_demo;


import java.lang.reflect.Proxy;

public class JdkProxyFactory {

    public Object getProxy(Object targetObject) {

        ClassLoader classLoader = targetObject.getClass().getClassLoader();
        Class<?>[] interfaces = targetObject.getClass().getInterfaces();
        JdkProxy jdkProxy = new JdkProxy(targetObject);
		
		//该方法接受三个参数：代理类的类加载器、代理类要实现的接口、InvocationHandler对象
        return Proxy.newProxyInstance(classLoader, interfaces, jdkProxy);
    }

    public static void main(String[] args) throws NoSuchMethodException {
		
		//设置系统参数，生成代理类的字节码文件（也可以在启动时以虚拟机参数的形式输入）
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        Book book = new Book();
        book.setBookName("战争与和平");
        Library library = new Library();

        //调用普通对象方法
        library.addBook(book);
        //调用代理对象方法
        JdkProxyFactory jdkProxyFactory = new JdkProxyFactory();
        BookService librarianProxy = (BookService) jdkProxyFactory.getProxy(library);//得到代理接口的实例对象
        librarianProxy.addBook(book);
    }
}
