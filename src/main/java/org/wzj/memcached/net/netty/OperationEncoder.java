package org.wzj.memcached.net.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.wzj.memcached.operation.Operation;

/**
 * Created by wens on 15-5-7.
 */
public class OperationEncoder extends MessageToByteEncoder<Operation> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Operation operaction, ByteBuf byteBuf) throws Exception {

        ReplyHandler replyHandler = channelHandlerContext.pipeline().get(ReplyHandler.class);

        byteBuf.writeBytes(operaction.buildCmd());

        if (replyHandler != null) {
            replyHandler.pushWaitingForReplyQueue(operaction);
        }

    }


}
