package org.wzj.memcached;

import java.util.Date;
import java.util.Iterator;

import org.wzj.memcached.BaseOperation.Callback;


/**
 * 
 * @author Wen
 *
 */
public class OperationFactory {
	
	private int maxLoopNum ;
	
	private MemcachedNodeFactory memcachedNodeFactory ;

	public OperationFactory(MemcachedNodeFactory memcachedNodeFactory) {
		this.memcachedNodeFactory = memcachedNodeFactory ;
		maxLoopNum = this.memcachedNodeFactory.getAllMemcachedNodes().size() * 100 ;
	}
	
	private MemcachedNode  getAvailableMemcachedNode(String key ){
		
		int i  = 0 ;
		
		Iterator<MemcachedNode> iterator = this.memcachedNodeFactory.locate(key) ;
		
		
		while(iterator.hasNext() ){
			MemcachedNode node = iterator.next();
			
			if(node.isConnected() ){
				return node ;
			}else{
				i++ ;
				
				try {
					Thread.sleep(100 * i );
					
					if(node.isConnected()) return node ;
					
				} catch (InterruptedException e) {
				}
				
				if(i >= maxLoopNum ){
					throw new IllegalStateException("not has available server ") ;
				}
				
			}
		}
		
		
		return null ;
	}

	public Operaction createStore(String cmd, String key, Object value,
			Date expiry, final OperationFutrue futrue) {

		if (cmd == null) {
			throw new NullPointerException("cmd not must be  null !");
		}

		if (expiry == null)
			expiry = new Date(0);

		int expiryTime = (int) (expiry.getTime() / 1000);

		if (MemcachedConstants.CMD_ADD == cmd) {

			Operaction operation = new AddOperation(key, value, expiryTime,
					new Callback() {

						@Override
						public void complete(Object msg) {
							futrue.setMsg(msg);
						}
					});
			futrue.setOperation(operation);
			
			operation.setMemcachedNode(getAvailableMemcachedNode(key)) ;
			
			return operation;
		} else if (MemcachedConstants.CMD_SET == cmd) {
			Operaction operation = new SetOperation(key, value, expiryTime,
					new Callback() {

						@Override
						public void complete(Object msg) {
							futrue.setMsg(msg);
						}
					});
			futrue.setOperation(operation);

			operation.setMemcachedNode(getAvailableMemcachedNode(key)) ;
			return operation;

		} else if (MemcachedConstants.CMD_REPLACE == cmd) {
			Operaction operation = new ReplaceOperation(key, value, expiryTime,
					new Callback() {

						@Override
						public void complete(Object msg) {
							futrue.setMsg(msg);
						}
					});
			futrue.setOperation(operation);

			operation.setMemcachedNode(getAvailableMemcachedNode(key)) ;
			
			return operation;
		} else {
			throw new IllegalArgumentException(
					"Cmd may be 'add' or 'set' or 'replace' ");
		}
	}

}
