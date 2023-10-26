package com.xdu.annotation;

import java.lang.annotation.*;

/**
 * @Author tyeerth
 * @Date 2023/10/25 20:43
 * @Description
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
//@Import(CustomScannerRegistrar.class)
@Documented
public @interface RpcScan {
    String[] ScanPackage();
}
