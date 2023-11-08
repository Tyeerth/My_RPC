package com.xdu;

import com.xdu.Enum.RpcConfigEnum;
import org.junit.Test;

/**
 * @Author tyeerth
 * @Date 2023/11/8 21:02
 * @Description
 */
public class testEnum {
    @Test
    public void testE(){
        System.out.println(RpcConfigEnum.RPC_CONFIG_PATH.compareTo(RpcConfigEnum.ZK_ADDRESS));
    }
}
