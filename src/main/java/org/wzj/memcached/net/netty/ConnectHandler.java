package org.wzj.memcached.net.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wens on 15-5-8.
 */
public class ConnectHandler extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(ConnectHandler.class);

    private ConnectListener connectListener;

    public ConnectHandler(ConnectListener connectListener) {
        this.connectListener = connectListener;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        connectListener.onConnected(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        connectListener.onDisconnected(ctx.channel());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        connectListener.onExceptionCaught(ctx.channel());
        log.error("Error on " + ctx.channel(), cause);
        ctx.channel().close();
    }
}
