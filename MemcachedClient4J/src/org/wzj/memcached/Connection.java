package org.wzj.memcached;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

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
	
	private   AtomicBoolean init = new AtomicBoolean(false) ;

	private Connection() {

	}

	public ChannelFuture connect(InetSocketAddress remoteAdd) {
		
		if(init.get() == false ){
			throw new RuntimeException(" Not initialized ") ;
		}
		
		return boostrap.connect(remoteAdd);
	}

	public static Connection getInstance() {
		return INSTANCE;
	}
	
	public void shutdown(){
		boostrap.releaseExternalResources();
	}

	public void init(String[] servers) {
		synchronized(init){
			if(init.get() == false ){
			channelFactory = new NioClientSocketChannelFactory(
					Executors.newCachedThreadPool(),
					Executors.newCachedThreadPool(),  servers.length   );
			boostrap = new ClientBootstrap(channelFactory);

			boostrap.setPipelineFactory(new MemcachedChannelPipelineFactory());
			
			init.set(true);
		}
		
		}
		
	}

}
