package org.wzj.memcached;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * 
 * @author Wen
 * 
 */
public class MemcachedClient {
	private OperationFactory oFactory;
	private MemcachedNodeFactory memcachedNodeFactory;

	public MemcachedClient(String server) {
		this(new String[] { server });
	}

	public MemcachedClient(String[] servers) {
		memcachedNodeFactory = new MemcachedNodeFactory(servers);
		oFactory = new OperationFactory(memcachedNodeFactory);
	}

	public OperationFutrue ayncSet(String key, Object value) {
		return this.ayncStore(MemcachedConstants.CMD_SET, key, value, null);
	}

	public OperationFutrue ayncSet(String key, Object value, Date expiry) {

		return this.ayncStore(MemcachedConstants.CMD_SET, key, value, expiry);
	}

	public OperationFutrue ayncAdd(String key, Object value) {
		return this.ayncStore(MemcachedConstants.CMD_ADD, key, value, null);
	}

	public OperationFutrue ayncAdd(String key, Object value, Date expiry) {
		return this.ayncStore(MemcachedConstants.CMD_ADD, key, value, expiry);
	}

	public OperationFutrue ayncReplace(String key, Object value) {

		return this.ayncStore(MemcachedConstants.CMD_REPLACE, key, value, null);
	}

	public OperationFutrue ayncReplace(String key, Object value, Date expiry) {

		return this.ayncStore(MemcachedConstants.CMD_REPLACE, key, value, null);
	}

	public OperationFutrue get(String key) {

		return this.ayncGet(MemcachedConstants.CMD_GET, new String[] { key });
	}

	public OperationFutrue gets(String... keys) {

		return this.ayncGet(MemcachedConstants.CMD_GET, keys);

	}

	public OperationFutrue ayncDelete(String key, Date expiry) {

		return null;
	}

	public OperationFutrue ayncIncr(String key, long inc) {
		return null;
	}

	public OperationFutrue ayncDecr(String key, long inc) {
		return null;
	}

	private OperationFutrue ayncStore(String cmd, String key, Object value,
			Date expiry) {
		
		CountDownLatch latch = new CountDownLatch(1);
		OperationFutrue futrue = new OperationFutrue(latch);
		
		Operaction operation = oFactory.createStore(cmd, key, value, expiry,
				futrue);

		operation.initialize();

		return futrue;
	}

	private OperationFutrue ayncGet(String cmd, String[] keys) {

		return null;
	}
	
	public void shutdown(){
		Collection<MemcachedNode> allMemcachedNodes = memcachedNodeFactory.getAllMemcachedNodes()  ;
		
		for(MemcachedNode node  : allMemcachedNodes){
			node.destory(); 
		}
		
		Connection.getInstance().shutdown(); 
	}

}
