package org.wzj.memcached.net.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.wzj.memcached.operation.Operation;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Wen
 */
public class ReplyHandler extends ByteToMessageDecoder {

    private BlockingQueue<Operation> waitingForReplyQueue = new LinkedBlockingDeque<Operation>();

    private volatile Operation operaction;


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        if (operaction == null || operaction.isComplete()) {
            operaction = waitingForReplyQueue.take();
        }

        if (operaction.getDataBlockLength() > 0) { // read data block
            if (byteBuf.readableBytes() >= operaction.getDataBlockLength() + 2 /*\r\n*/) {
                byte[] body = new byte[operaction.getDataBlockLength()];
                byteBuf.readBytes(body);
                byteBuf.skipBytes(2);
                operaction.setDataBlock(body);
            }
        } else {
            String line = readLine(byteBuf);
            if (line != null)
                operaction.setReply(line);
        }
    }

    private String readLine(ByteBuf byteBuf) {
        byteBuf.markReaderIndex();

        int reader_index = byteBuf.readerIndex();

        while (byteBuf.isReadable()) {

            byte b = byteBuf.readByte();

            if (b == '\r' && byteBuf.isReadable() && byteBuf.readByte() == '\n') {
                int headerLength = byteBuf.readerIndex() - reader_index;
                byte[] bb = new byte[headerLength];
                byteBuf.resetReaderIndex();
                byteBuf.readBytes(bb);
                String line = new String(bb, 0, bb.length - 2);
                return line;
            }
        }

        byteBuf.resetReaderIndex();
        return null;
    }

    public void pushWaitingForReplyQueue(Operation operaction) {

        waitingForReplyQueue.offer(operaction);
    }
}
