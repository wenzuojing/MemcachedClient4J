package org.wzj.memcached;

import java.util.concurrent.atomic.AtomicReference;


/**
 * 
 * @author Wen
 *
 */
public abstract class BaseOperation extends ReadLine implements Operaction {

	protected String cmd;

	protected AtomicReference<Status> status = new AtomicReference<Status>();

	protected MemcachedNode node;
	
	protected Callback callback ;

	/**
	 * @param cmd
	 */
	public BaseOperation(String cmd , Callback  callback) {
		this.cmd = cmd;
		status.set(Status.INITIALIZE);
		this.callback  = callback ; 
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Status getStatus() {
		return status.get();
	}

	public void setStatus(Status newStatus) {
		status.set(newStatus);
	}

	@Override
	public boolean isCancelled() {

		if (status.get() == Status.CANCELLE) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isComplete() {
		if (status.get() == Status.COMPLETE) {
			return true;
		}

		return false;
	}

	@Override
	public synchronized boolean cancel() {

		if (status.get() == Status.INITIALIZE) {

			status.set(Status.CANCELLE);

			return true;
		}

		return false;
	}


	public void setMemcachedNode(MemcachedNode node) {
		this.node = node;
	}

	public static interface Callback {
		void complete(Object msg);
	}

}
