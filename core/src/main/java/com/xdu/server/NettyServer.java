package com.xdu.server;

import com.xdu.config.RpcServiceConfig;
import com.xdu.factory.SingleFactory;
import com.xdu.protocols.RpcMessageDecoder;
import com.xdu.protocols.RpcMessageEncoder;
import com.xdu.server.handler.NettyRpcServerHandler;
import com.xdu.utils.RuntimeUtil;
import com.xdu.utils.ThreadPoolFactoryUtil;
import com.xdu.zk.ServiceProvider;
import com.xdu.zk.impl.ZkServiceProviderImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * @Author tyeerth
 * @Date 2023/10/25 21:44
 * @Description
 */
@Slf4j
@Component
public class NettyServer {
    public static final int PORT = 9998;
    private final ServiceProvider serviceProvider = SingleFactory.getInstance(ZkServiceProviderImpl.class);
    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    @SneakyThrows
    public void start(){
        String host = InetAddress.getLocalHost().getHostAddress();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        // 创建线程池来执行处理逻辑
        DefaultEventExecutorGroup serviceHandlerGroup = new DefaultEventExecutorGroup(
                RuntimeUtil.cpus() * 2,
                ThreadPoolFactoryUtil.createThreadFactory("service-handler-group", false)
        );
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE, true)

//        handler在初始化时就会执行，而childHandler会在客户端成功connect后才执行，这是两者的区别。
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        //30s内没有收到客户端请求就关闭
                        pipeline.addLast(new IdleStateHandler(15,0,0, TimeUnit.SECONDS));
                        pipeline.addLast(new RpcMessageDecoder());
                        pipeline.addLast(new RpcMessageEncoder());
                        // 这里的msg是客户端发送过来的
                        //实际业务的处理
                        pipeline.addLast(serviceHandlerGroup,new NettyRpcServerHandler());
                    }
                })
                .bind(host,PORT).sync()
        // 等待服务端监听端口关闭,等待服务端socket结束
        .channel()
                .closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        serviceHandlerGroup.shutdownGracefully();

    }
}
