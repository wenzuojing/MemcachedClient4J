package org.wzj.memcached.node;

import java.util.Collection;
import java.util.Iterator;


/**
 * 
 * @author Wen
 *
 */
public interface  NodeLocator {
	
	  MemcachedNode getPrimary(String key);

	  Iterator<MemcachedNode> getSequence(String key);

	  Collection<MemcachedNode> getAll();

}
