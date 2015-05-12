package org.wzj.memcached.net.netty;

import io.netty.channel.Channel;

/**
 * Created by wens on 15-5-8.
 */
public interface ConnectListener {

    void onConnected(Channel channel);

    void onDisconnected(Channel channel);


    void onExceptionCaught(Channel channel);
}
