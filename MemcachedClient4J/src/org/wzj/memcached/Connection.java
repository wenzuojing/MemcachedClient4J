package org.wzj.memcached;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;


/**
 * 
 * @author Wen
 *
 */
public class Connection {

	private static final Connection INSTANCE = new Connection();

	private ChannelFactory channelFactory;

	private ClientBootstrap boostrap;

	private Connection() {

		channelFactory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool(), 5);
		boostrap = new ClientBootstrap(channelFactory);

		boostrap.setPipelineFactory(new MemcachedChannelPipelineFactory());

	}

	public ChannelFuture connect(InetSocketAddress remoteAdd) {
		return boostrap.connect(remoteAdd);
	}

	public static Connection getInstance() {
		return INSTANCE;
	}

}
