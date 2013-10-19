package org.wzj.memcached;

/**
 * 
 * @author Wen
 *
 */
public class SetOperation  extends StoreOperation {

	public SetOperation(String key , Object value , int expiry ,Callback callback ){
		
		super(MemcachedConstants.CMD_SET , key, value , expiry ,callback) ;
	}
	
	
}
