package com.xdu.server.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author tyeerth
 * @Date 2023/10/25 20:43
 * @Description
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(CustomScannerRegistrar.class) //使用@import可以导入其他类，这样就可以在注解值里使用其他类的类型。如果不使用@import，那么在注解值里只能使用当前注解所在的包下的类型，无法使用其他包的类型
@Documented
public @interface RpcScan {
    String[] ScanPackage();
}
