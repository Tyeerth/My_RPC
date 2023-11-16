package com.xdu.client.handler;

/**
 * @author tyeerth
 * @date 2023/11/16 - 上午10:21
 * @description
 */
public class Animal {
    public final String name ;
    public void eat(){
        System.out.println("animal");
    }
    public Animal(){
        this.name = "parent animal";
    }
    public void sayHello(){
        System.out.println(name);
    }

}
