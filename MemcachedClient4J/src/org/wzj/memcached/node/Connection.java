package org.wzj.memcached.node;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.wzj.memcached.MemcachedChannelPipelineFactory;


/**
 * 
 * @author Wen
 *
 */
public class Connection {

	private static final Connection INSTANCE = new Connection();

	private ChannelFactory channelFactory;

	private ClientBootstrap boostrap;
	
	private   boolean init = false ;

	private Connection() {

	}

	public ChannelFuture connect(InetSocketAddress remoteAdd) {
		
		if(init == false ){
			throw new RuntimeException(" Not initialized ") ;
		}
		
		return boostrap.connect(remoteAdd);
	}

	public static Connection getInstance() {
		return INSTANCE;
	}
	
	public void shutdown(){
		boostrap.releaseExternalResources();
		
		init = false ;
	}

	public void init(String[] servers) {
		
		if(init) return ;
		
		channelFactory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool(),  servers.length   );
		boostrap = new ClientBootstrap(channelFactory);

		boostrap.setPipelineFactory(new MemcachedChannelPipelineFactory());
		
		init  = true ;
	}

}
