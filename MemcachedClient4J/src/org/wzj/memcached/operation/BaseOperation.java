package org.wzj.memcached.operation;

import java.util.concurrent.atomic.AtomicReference;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.wzj.memcached.ReadLine;
import org.wzj.memcached.ReplyHandler;
import org.wzj.memcached.node.MemcachedNode;


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
	
	protected class WriteCompleteListener implements  ChannelFutureListener {
		@Override
		public void operationComplete(ChannelFuture future)
				throws Exception {
			ReplyHandler reply = (ReplyHandler) future.getChannel()
					.getPipeline().get("reply");
			reply.enqueue(BaseOperation.this);

		}
	}

}
