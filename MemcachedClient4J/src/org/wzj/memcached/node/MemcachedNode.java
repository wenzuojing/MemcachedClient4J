package org.wzj.memcached.node;

import java.net.InetSocketAddress;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;


/**
 * 
 * @author Wen
 *
 */
public class MemcachedNode {

	private Channel channel;


	private InetSocketAddress add;
	
	
	
	public MemcachedNode( String  host , int port ) {
		this(new InetSocketAddress(host , port)) ;
	}

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
	}

	public void destory() {
		if (channel != null){
			
			channel.close().awaitUninterruptibly() ;
		}

	}
	
	public String  getServerInfo(){
		return this.add.getHostName()+":"+this.add.getPort() ;
	}
}
