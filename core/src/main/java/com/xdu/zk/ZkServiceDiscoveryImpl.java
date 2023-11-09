package com.xdu.zk;

import com.xdu.Enum.RpcErrorMessageEnum;
import com.xdu.Exception.RpcException;
import com.xdu.message.RpcRequest;
import com.xdu.zk.util.CuratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author tyeerth
 * @date 2023/11/9 - 下午4:41
 * @description
 */
@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery{
    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
        if (serviceUrlList.isEmpty()){
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND,rpcServiceName);
        }
        //TODO load Balance not completed

        // myrpc/rpcServiceName/inetSocketAddress
        String targetServiceUrl = serviceUrlList.get(0);
        log.info("Successfully found the service address:[{}]", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);

    }
}
