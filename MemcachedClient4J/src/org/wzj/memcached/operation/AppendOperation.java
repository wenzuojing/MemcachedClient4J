package org.wzj.memcached.operation;

import org.wzj.memcached.MemcachedConstants;

/**
 * 
 * @author Wen  
 *
 */
public class AppendOperation  extends StoreOperation {

	public AppendOperation(String key , Object value , int expiry ,Callback callback ){
		super(MemcachedConstants.CMD_ADD , key, value , expiry, callback) ;
	}
}
