package com.xdu.client.handler;

import com.xdu.client.NettyRpcClient;
import com.xdu.constants.RpcConstants;
import com.xdu.factory.SingleFactory;
import com.xdu.message.RpcMessage;
import com.xdu.message.RpcResponse;
import com.xdu.server.handler.NettyRpcServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author tyeerth
 * @date 2023/11/2 - 上午11:16
 * @description
 */
@Slf4j
public class NettyRpcClientHandler extends ChannelInboundHandlerAdapter {
    private final NettyRpcClient nettyRpcClient;
    public NettyRpcClientHandler(){
        this.nettyRpcClient = SingleFactory.getInstance(NettyRpcClient.class);
    }


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
    /**
     * 空闲状态事件
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent) evt).state();
            // 写空闲
            if (state == IdleState.WRITER_IDLE){
                log.info("write idle happen [{}]",ctx.channel());
                log.info("write idle happen [{}]",ctx.channel().remoteAddress());
                //send PING
                Channel channel = nettyRpcClient.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
                RpcMessage rpcMessage = new RpcMessage();
                rpcMessage.setMessageType(RpcConstants.HEARTBEAT_REQUEST_TYPE);
                rpcMessage.setData(RpcConstants.PING);
                channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }else {
                super.userEventTriggered(ctx, evt);
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}
