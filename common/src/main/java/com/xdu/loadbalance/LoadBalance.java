package com.xdu.loadbalance;

import com.xdu.message.RpcRequest;

import java.util.List;

/**
 * @author tyeerth
 * @date 2023/11/17 - 上午9:38
 * @description
 */
public interface LoadBalance {
    String selectServiceAddress(List<String> serviceUrlList, RpcRequest rpcRequest);
}
