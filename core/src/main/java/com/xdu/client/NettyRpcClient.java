package com.xdu.client;


import com.xdu.client.handler.NettyRpcClientHandler;
import com.xdu.constants.RpcConstants;
import com.xdu.message.RpcMessage;
import com.xdu.message.RpcRequest;
import com.xdu.message.RpcResponse;
import com.xdu.protocols.RpcMessageDecoder;
import com.xdu.protocols.RpcMessageEncoder;
import com.xdu.transport.RpcRequestTransport;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author tyeerth
 * @date 2023/11/3 - 下午2:56
 * @description
 */
@Slf4j
public class NettyRpcClient implements RpcRequestTransport {
    /**
     * 注意final关键字修饰的变量需要在构造方法中进行初始化
     */
    public static final int PORT = 9998;
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;
    public NettyRpcClient(){
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                //The timeout period of the connection
        // If this time is exceeded or the connection cannot be established,the connection fails
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        pipeline.addLast(new RpcMessageEncoder());
                        pipeline.addLast(new RpcMessageDecoder());
                        pipeline.addLast(new NettyRpcClientHandler());
                    }
                });
    }

    /**
     * conect to server and get the channel,so you can send Rpcmessage
     * @param inetSocketAddress
     * @return
     */
    public Channel doConnect(InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                log.info("The client has connected {}", inetSocketAddress.toString());
                completableFuture.complete(channelFuture.channel());
            } else {
                throw new IllegalAccessException();
            }
        });
        return completableFuture.get();
    }
    public Object sendRpcRequest(RpcRequest rpcRequest) {

        // build return value
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
            Channel channel = doConnect(new InetSocketAddress(host, PORT));
            if (channel.isActive()){
                RpcMessage rpcMessage = RpcMessage.builder().data(rpcRequest)
                        .messageType(RpcConstants.REQUEST_TYPE).build();
                channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        log.info("client send message: [{}]", rpcMessage);
                    } else {
                        future.channel().close();
                        resultFuture.completeExceptionally(future.cause());
                        log.error("Send failed:", future.cause());
                    }
                });
            }
        } catch (UnknownHostException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return resultFuture;
    }
}
