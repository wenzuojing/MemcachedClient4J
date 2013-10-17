package org.wzj.memcached;

import java.net.InetSocketAddress;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;


/**
 * 
 * @author Wen
 *
 */
public class MemcachedNode {

	private Channel channel;


	private InetSocketAddress add;

	public MemcachedNode( InetSocketAddress add) {

		this.add = add;
		init();
	}

	public Channel getChannel() {

		return channel;
	}

	public boolean isConnected() {
		return channel.isConnected();
	}

	public void init() {
		
		ChannelFuture channelFuture = Connection.getInstance().connect(this.add);
		
		channelFuture.awaitUninterruptibly() ;

		channel = channelFuture.getChannel();

		channel.getCloseFuture().addListener(new ChannelFutureListener() {

			@Override
			public void operationComplete(ChannelFuture future)
					throws Exception {

				destory();
			}
			
			
		});

	}

	public void destory() {
		if (channel != null)
			channel.close();

	}
}
