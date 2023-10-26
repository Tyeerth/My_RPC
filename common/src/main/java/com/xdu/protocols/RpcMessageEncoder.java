package com.xdu.protocols;

import cn.hutool.core.util.ObjectUtil;
import com.xdu.constants.RpcConstants;
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
 *  * custom protocol decoder
 *  * <p>
 *  * <pre>
 *    0     1     2     3     4        5     6     7     8         9          10      11     12  13  14   15 16
 *    +-----+-----+-----+-----+--------+----+----+----+------+-----------+-------+----- --+-----+-----+-------+
 *    |   magic   code        |version | full length         | messageType| codec|compress|    RequestId       |
 *    +-----------------------+--------+---------------------+-----------+-----------+-----------+------------+
 *    |                                                                                                       |
 *    |                                         body                                                          |
 *    |                                                                                                       |
 *    |                                        ... ...                                                        |
 *   +-------------------------------------------------------------------------------------------------------+
 *  4B  magic code（魔法数）   1B version（版本）   4B full length（消息长度）    1B messageType（消息类型）
 *  1B compress（压缩类型） 1B codec（序列化类型）    4B  requestId（请求的Id）
 *  body（object类型数据）
 *   </pre>
 */
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);
    @Override //writing rpcMessage to byteBuf
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(RpcConstants.MAGIC_NUMBER);
        byteBuf.writeByte(RpcConstants.VERSION);
//        指定下一次写入操作的起始位置
        // leave a place to write the value of full length,save 5 bytes
        byteBuf.writerIndex(byteBuf.writerIndex() + 4);

        byteBuf.writeByte(rpcMessage.getMessageType());
        byteBuf.writeByte(rpcMessage.getCodec());
        // leave a place for compress
        byteBuf.writerIndex(byteBuf.writerIndex()+1);
        // 32 bite data
        byteBuf.writeInt(ATOMIC_INTEGER.getAndIncrement());

        byte[] bodyBytes = null;
        int fullLength = RpcConstants.HEAD_LENGTH;

        // if messageType is not heartbeat message,then fullLength = head_length+body_length
        if (rpcMessage.getMessageType() != RpcConstants.HEARTBEAT_REQUEST_TYPE
                && rpcMessage.getMessageType() != RpcConstants.HEARTBEAT_RESPONSE_TYPE){
        // serial data
            Object data = rpcMessage.getData();
            bodyBytes = ObjectUtil.serialize(data);
            fullLength += bodyBytes.length;
        }

        if(bodyBytes != null){
            byteBuf.writeBytes(bodyBytes);
        }

        int writeIndex = byteBuf.writerIndex();
        byteBuf.writerIndex(writeIndex - fullLength + RpcConstants.MAGIC_NUMBER.length + 1);
        byteBuf.writeInt(fullLength);
        byteBuf.writerIndex(writeIndex);
    }
}
