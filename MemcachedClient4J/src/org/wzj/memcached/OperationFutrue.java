package org.wzj.memcached;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 
 * @author Wen
 *
 */
public class OperationFutrue implements Future<Object> {
	

	private CountDownLatch latch;

	private Operaction operation;
	
	private AtomicReference<Object> msg = new AtomicReference<Object>() ;
	
	public OperationFutrue(){
		//this.latch = new 
	}

	public OperationFutrue(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return operation.cancel();
	}

	@Override
	public boolean isCancelled() {
		return operation.isCancelled();
	}

	@Override
	public boolean isDone() {

		return operation.isComplete();

	}

	@Override
	public Object get() throws InterruptedException, ExecutionException {
		//latch.await();
		try {
			return this.get(60, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			throw new RuntimeException("operation timeout ") ;
		}
	}

	@Override
	public Object get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		latch.await(timeout, unit);
		return this.msg.get();
	}

	public Operaction getOperation() {
		return operation;
	}

	public void setOperation(Operaction operation) {
		this.operation = operation;
	}
	
	public void setMsg(Object msg ){
		
		this.msg.set(msg) ;
		
		latch.countDown() ;
	}

}
