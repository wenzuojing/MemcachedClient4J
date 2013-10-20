package org.wzj.memcached;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.wzj.memcached.future.OperationFutrue;
import org.wzj.memcached.node.Connection;
import org.wzj.memcached.node.MemcachedNode;
import org.wzj.memcached.node.MemcachedNodeFactory;
import org.wzj.memcached.operation.Operaction;
import org.wzj.memcached.operation.OperationFactory;

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

	public OperationFutrue asynSet(String key, Object value) {
		return this.asynStore(MemcachedConstants.CMD_SET, key, value, null);
	}

	public OperationFutrue asynSet(String key, Object value, Date expiry) {

		return this.asynStore(MemcachedConstants.CMD_SET, key, value, expiry);
	}

	public OperationFutrue asynAdd(String key, Object value) {
		return this.asynStore(MemcachedConstants.CMD_ADD, key, value, null);
	}

	public OperationFutrue asynAdd(String key, Object value, Date expiry) {
		return this.asynStore(MemcachedConstants.CMD_ADD, key, value, expiry);
	}

	public OperationFutrue asynReplace(String key, Object value) {

		return this.asynStore(MemcachedConstants.CMD_REPLACE, key, value, null);
	}

	public OperationFutrue asynReplace(String key, Object value, Date expiry) {

		return this.asynStore(MemcachedConstants.CMD_REPLACE, key, value, null);
	}
	
	public OperationFutrue asynAppend(String key, Object value) {

		return this.asynStore(MemcachedConstants.CMD_APPEND, key, value, null);
	}

	public OperationFutrue asynAppend(String key, Object value, Date expiry) {

		return this.asynStore(MemcachedConstants.CMD_APPEND, key, value, null);
	}
	
	public OperationFutrue asynStats(String server ) {
		CountDownLatch latch = new CountDownLatch(1);
		OperationFutrue futrue = new OperationFutrue(latch);

		Operaction operation = oFactory.createStats(server , futrue);

		try {
			operation.initialize();
		} catch (IOException e) {
			throw new RuntimeException(" Failed to initialize");
		}

		return futrue;
	}
	
	
	public OperationFutrue asynFlush(String server ) {
		CountDownLatch latch = new CountDownLatch(1);
		OperationFutrue futrue = new OperationFutrue(latch);

		Operaction operation = oFactory.createFlush(server , futrue);

		try {
			operation.initialize();
		} catch (IOException e) {
			throw new RuntimeException(" Failed to initialize");
		}

		return futrue;
	}

	public OperationFutrue asynGet(String key) {

		return this.asynGet(new String[] { key });
	}

	public OperationFutrue asynGets(String... keys) {

		return this.asynGet(keys);

	}

	public OperationFutrue asynDelete(String key, Date expiry) {

		CountDownLatch latch = new CountDownLatch(1);
		OperationFutrue futrue = new OperationFutrue(latch);

		Operaction operation = oFactory.createDelete( key, expiry,futrue);

		try {
			operation.initialize();
		} catch (IOException e) {
			throw new RuntimeException(" Failed to initialize");
		}

		return futrue;
	}

	public OperationFutrue asynIncr(String key, long inc) {
		return asyncIncrOrDecr(MemcachedConstants.CMD_INCR, key, inc);
	}

	public OperationFutrue asynDecr(String key, long inc) {
		return asyncIncrOrDecr(MemcachedConstants.CMD_DECR, key, inc);
	}

	private OperationFutrue asynStore(String cmd, String key, Object value,
			Date expiry) {

		CountDownLatch latch = new CountDownLatch(1);
		OperationFutrue futrue = new OperationFutrue(latch);

		Operaction operation = oFactory.createStore(cmd, key, value, expiry,
				futrue);

		try {
			operation.initialize();
		} catch (IOException e) {
			throw new RuntimeException(" Failed to initialize");
		}

		return futrue;
	}

	private OperationFutrue asynGet(String[] keys) {

		CountDownLatch latch = new CountDownLatch(1);
		OperationFutrue futrue = new OperationFutrue(latch);

		Operaction operation = oFactory.createGet(keys, futrue);
		try {
			operation.initialize();
		} catch (IOException e) {
			throw new RuntimeException(" Failed to initialize");
		}
		return futrue;
	}

	private OperationFutrue asyncIncrOrDecr(String cmd, String key, long inc) {

		CountDownLatch latch = new CountDownLatch(1);
		OperationFutrue futrue = new OperationFutrue(latch);

		Operaction operation = oFactory.createIncrOrDecr(cmd ,key , inc, futrue);
		
		try {
			operation.initialize();
		} catch (IOException e) {
			throw new RuntimeException(" Failed to initialize");
		}
		return futrue;
	}

	public void shutdown() {
		Collection<MemcachedNode> allMemcachedNodes = memcachedNodeFactory
				.getAllMemcachedNodes();

		for (MemcachedNode node : allMemcachedNodes) {
			node.destory();
		}

		Connection.getInstance().shutdown();
	}

}
