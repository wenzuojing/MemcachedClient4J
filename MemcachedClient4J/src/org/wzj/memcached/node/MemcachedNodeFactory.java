package org.wzj.memcached.node;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Iterator;

/**
 * 
 * @author Wen
 *
 */
public class MemcachedNodeFactory {

	private NodeLocator locator;

	public MemcachedNodeFactory(String[] servers) {

		if (servers == null || servers.length == 0) {
			throw new IllegalArgumentException("Must include at least one node");
		}
		
		Connection.getInstance().init(servers) ;

		MemcachedNode[] nodes = new MemcachedNode[servers.length];
		
		for(int i = 0 , len  = nodes.length ; i < len ;i++){
			String server = servers[i];
			
			String[] split = server.split("[:|(\\s)+]") ;
			
			if(split.length != 2 ){
				throw new IllegalArgumentException(server + " is not illegal ") ;
			}
			
			nodes[i] = new MemcachedNode(new InetSocketAddress(split[0], Integer.parseInt(split[1]))) ;
		}
		
		
		
		locator = new ArrayModNodeLocator(nodes);
	}

	public Iterator<MemcachedNode> locate(String key) {
		return locator.getSequence(key);
	}
	
	public Collection<MemcachedNode> getAllMemcachedNodes(){
		return locator.getAll() ;
	}
	
	

}
