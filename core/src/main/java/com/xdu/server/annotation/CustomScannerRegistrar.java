package com.xdu.server.annotation;

import com.xdu.server.NettyServer;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * @Author tyeerth
 * @Date 2023/10/29 11:05
 * @Description 实现自定义包扫描注解
 */
public class CustomScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private ResourceLoader resourceLoader;
    private static final String BASE_PACKAGE_ATTRIBUTE_NAME = "basePackage";


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        //get the attributes from the registry
        AnnotationAttributes rpcScanAnnotationAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(RpcScan.class.getName()));
        String[] rpcScanBasePackages = new String[1];
        if (rpcScanAnnotationAttributes != null) {
            rpcScanBasePackages = rpcScanAnnotationAttributes.getStringArray(BASE_PACKAGE_ATTRIBUTE_NAME);
        }
        //如果没有值，就把当前的类传入StringArray中
        if (rpcScanBasePackages.length == 0) {
            rpcScanBasePackages = new String[]{((StandardAnnotationMetadata) annotationMetadata).getIntrospectedClass().getPackage().getName()};
        }
//        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
//        RootBeanDefinition beanDefinition = new RootBeanDefinition();
//        beanDefinition.setBeanClass(NettyServer.class);
//        registry.registerBeanDefinition("NettyServer",beanDefinition);
        MapperBeanDefinitionScanner scanner = new MapperBeanDefinitionScanner(registry, false);
        scanner.setResourceLoader(resourceLoader);
        scanner.registerFilters();
        //这里增加对RpcScan注解类的扫描
        scanner.addIncludeFilter(new AnnotationTypeFilter(RpcScan.class));
        //测试直接写死，可改成要scan的目录
        scanner.doScan(rpcScanBasePackages);
    }
}
