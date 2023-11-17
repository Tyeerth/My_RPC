package com.xdu.loadbalance;

import cn.hutool.core.collection.CollectionUtil;
import com.xdu.message.RpcRequest;

import java.util.Collections;
import java.util.List;

/**
 * @author tyeerth
 * @date 2023/11/17 - 上午9:37
 * @description
 */
public abstract class AbstractLoadBalance implements LoadBalance{
    @Override
    public String selectServiceAddress(List<String> serviceUrlList, RpcRequest rpcRequest) {
        if (CollectionUtil.isEmpty(serviceUrlList)){
            return null;
        }else if (serviceUrlList.size() == 1){
            return serviceUrlList.get(0);
        }else {

            return doselect(serviceUrlList,rpcRequest);
        }
    }

    protected abstract String doselect(List<String> serviceUrlList, RpcRequest rpcRequest);
}
