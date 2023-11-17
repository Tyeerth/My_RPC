package com.xdu.loadbalance.loadbalancer;

import com.xdu.loadbalance.AbstractLoadBalance;
import com.xdu.message.RpcRequest;

import java.util.List;
import java.util.Random;

/**
 * @author tyeerth
 * @date 2023/11/17 - 上午9:53
 * @description
 */
public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    protected String doselect(List<String> serviceUrlList, RpcRequest rpcRequest) {
        int size  = serviceUrlList.size();
        Random random = new Random();
        return serviceUrlList.get(random.nextInt(size));
    }
}
