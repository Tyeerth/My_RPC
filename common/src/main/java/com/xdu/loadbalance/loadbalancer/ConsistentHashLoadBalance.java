package com.xdu.loadbalance.loadbalancer;

import cn.hutool.crypto.digest.MD5;
import com.xdu.loadbalance.AbstractLoadBalance;
import com.xdu.message.RpcRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tyeerth
 * @date 2023/11/17 - 上午10:11
 * @description
 */
public class ConsistentHashLoadBalance extends AbstractLoadBalance {

    private final ConcurrentHashMap<String,ConsistentHashSelector> selectors = new ConcurrentHashMap<>();
    @Override
    protected String doselect(List<String> serviceUrlList, RpcRequest rpcRequest) {
        //IdentityHashCode，作为唯一标识，来查看是否已经存在于服务节点中
        int identityHashCode = System.identityHashCode(serviceUrlList);
        String rpcServiceName = rpcRequest.getRpcServiceName();
        ConsistentHashSelector consistentHashSelector = selectors.get(rpcServiceName);
        // 节点不存在则进行创建
        if (consistentHashSelector == null || consistentHashSelector.identityHashCode != identityHashCode){
            selectors.put(rpcServiceName,new ConsistentHashSelector(serviceUrlList,160,identityHashCode));
            consistentHashSelector = selectors.get(rpcServiceName);
        }
        return consistentHashSelector.select(rpcServiceName + Arrays.stream(rpcRequest.getParameters()));
    }
    static class ConsistentHashSelector{
        private final TreeMap<Long,String> vituralInvokers;

        private final int identityHashCode;

        /**
         *  为每一个url创建replicaNumber个虚拟节点,实际上访问的还是url节点
         * @param serviceUrlList
         * @param replicaNumber
         * @param identityHashCode
         */
        ConsistentHashSelector(List<String> serviceUrlList, int replicaNumber,int identityHashCode){
            this.vituralInvokers = new TreeMap<>();
            this.identityHashCode = identityHashCode;

            for (String url : serviceUrlList){
                for (int i = 0; i < replicaNumber / 4;i++){
                    byte[] digest = md5(url + i);
                    for (int h = 0; h < 4; h++){
                        long m = hash(digest,h);
                        vituralInvokers.put(m, url);
                    }
                }
            }

        }
        static long hash(byte[] digest, int idx) {
            return ((long) (digest[3 + idx * 4] & 255) << 24 | (long) (digest[2 + idx * 4] & 255) << 16 | (long) (digest[1 + idx * 4] & 255) << 8 | (long) (digest[idx * 4] & 255)) & 4294967295L;
        }
        private byte[] md5(String key) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
                byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
                md.update(bytes);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
            return md.digest();
        }

        public String select(String rpcServiceKey) {
            byte[] bytes = md5(rpcServiceKey);
            return selectForKey(hash(bytes,0));
        }

        private String selectForKey(long hashCode) {
            Map.Entry<Long, String> entry = vituralInvokers.tailMap(hashCode, true).firstEntry();

            if (entry == null) {
                entry = vituralInvokers.firstEntry();
            }

            return entry.getValue();
        }
    }
    public static void main(String[] args) {
        System.out.println(System.identityHashCode(12));
    }
}
