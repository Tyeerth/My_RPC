package com.xdu.server.handler;

import com.xdu.Enum.RpcResponseCodeEnum;
import com.xdu.client.handler.RpcRequstHandler;
import com.xdu.constants.RpcConstants;
import com.xdu.factory.SingleFactory;
import com.xdu.message.RpcMessage;
import com.xdu.message.RpcRequest;
import com.xdu.message.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author tyeerth
 * @Date 2023/10/26 21:33
 * @Description
 */
@Slf4j
public class NettyRpcServerHandler extends ChannelInboundHandlerAdapter {

    private final RpcRequstHandler requestHandler;

    public NettyRpcServerHandler(){
        this.requestHandler = SingleFactory.getInstance(RpcRequstHandler.class);
    }
    @Override  // 这里的msg是客户端发送过来的
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof RpcMessage){
            log.info("server receiver msg: {}",msg);
            byte messageType = ((RpcMessage) msg).getMessageType();
            // 重新封装一个RpcMessgae作为返回的实体类型
            RpcMessage rpcMessage = new RpcMessage();
            rpcMessage.setMessageType((byte) 0x04);
            rpcMessage.setCompress((byte)0x01);

            if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE){
                rpcMessage.setMessageType(RpcConstants.HEARTBEAT_RESPONSE_TYPE);
                rpcMessage.setData(RpcConstants.PONG);//因为这里封装的是消息返回实体，所以内容设置为pong
            }else {
                RpcRequest rpcRequest = (RpcRequest) ((RpcMessage) msg).getData();
                Object result = requestHandler.invokeTargetMethod(rpcRequest);
                log.info("server get data :{}",result.toString());
                String rpcServiceName = rpcRequest.getRpcServiceName();
                rpcMessage.setMessageType(RpcConstants.RESPONSE_TYPE);
                // 判断是否可以往通道中写入数据
                if (ctx.channel().isActive() && ctx.channel().isWritable()){
                    RpcResponse<String> rpcResponse = RpcResponse.success(result.toString(), rpcRequest.getRequestId());
                    rpcMessage.setData(rpcResponse);
                }else {
                    RpcResponse<Object> fail = RpcResponse.fail(RpcResponseCodeEnum.FAIL);
                    rpcMessage.setData(fail);
                    log.error("not writable channel");
                }
            }
            ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        }
        //Ensure that ByteBuf is released, otherwise there may be memory leaks
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
       if (evt instanceof IdleStateEvent){
           IdleState state = ((IdleStateEvent) evt).state();
           if (state == IdleState.READER_IDLE){
               log.info("idle 15 seconds , so closed the connection of channel[{}]",ctx.channel());
               ctx.close();
           }
       }else {

           super.userEventTriggered(ctx, evt);
       }
    }
}
