package com.xdu.client.handler;

import com.xdu.constants.RpcConstants;
import com.xdu.message.RpcMessage;
import com.xdu.message.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tyeerth
 * @date 2023/11/2 - 上午11:16
 * @description
 */
@Slf4j
public class NettyRpcClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            log.info("client receive msg :{}",msg);
            if (msg instanceof RpcMessage){
                RpcMessage rpcMessage = (RpcMessage)msg;
                byte messageType = rpcMessage.getMessageType();
                if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE){
                    log.info("heart [{}]", rpcMessage.getData());
                } else if (messageType == RpcConstants.RESPONSE_TYPE){
                    RpcResponse data = (RpcResponse) rpcMessage.getData();
                    log.info("RpcResponse data :{}",data);
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
