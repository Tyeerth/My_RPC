package com.xdu.factory;

import com.esotericsoftware.kryo.KryoException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author tyeerth
 * @date 2023/11/3 - 下午2:01
 * @description
 */
public class SingleFactory {
    private static final Map<String,Object> OBJECT_MAP = new ConcurrentHashMap<>() ;
    private SingleFactory(){}

    public static  <T> T getInstance(Class<T> c){
        if (c == null){
            throw new IllegalArgumentException();
        }
        String class_name = c.toString();
        if (OBJECT_MAP.containsKey(class_name)){
            return c.cast(OBJECT_MAP.get(class_name));
        }else {
            return c.cast(OBJECT_MAP.computeIfAbsent(class_name, key->{
                try {
                    return c.getDeclaredConstructor().newInstance();
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }));
        }
    }
}
