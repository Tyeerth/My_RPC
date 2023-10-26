package com.xdu.protocols;

import com.xdu.message.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.zookeeper.server.quorum.QuorumCnxManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author tyeerth
 * @Date 2023/10/26 10:46
 * @Description
 */
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf byteBuf) throws Exception {
        byteBuf.writableBytes();

    }
}
