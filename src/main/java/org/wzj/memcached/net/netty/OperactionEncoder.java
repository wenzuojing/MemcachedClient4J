package org.wzj.memcached.net.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.wzj.memcached.operation.Operaction;

/**
 * Created by wens on 15-5-7.
 */
public class OperactionEncoder extends MessageToByteEncoder<Operaction> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Operaction operaction, ByteBuf byteBuf) throws Exception {

        ReplyHandler replyHandler = channelHandlerContext.pipeline().get(ReplyHandler.class);

        synchronized (replyHandler) {

            byteBuf.writeBytes(operaction.buildCmd()) ;
            replyHandler.pushWaitingForReplyQueue(operaction);
        }

        channelHandlerContext.channel().read();

    }


}
