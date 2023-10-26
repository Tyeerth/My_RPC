package com.xdu.handler;

import com.xdu.message.RpcMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author tyeerth
 * @Date 2023/10/26 21:33
 * @Description
 */
public class NettyRpcServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof RpcMessage){

        }
    }
}
