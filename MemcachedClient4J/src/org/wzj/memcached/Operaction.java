package org.wzj.memcached;

import org.jboss.netty.buffer.ChannelBuffer;


/**
 * 
 * @author Wen
 *
 */
public interface Operaction {

	boolean isCancelled();

	boolean isComplete();

	boolean cancel();


	void initialize();

	Status getStatus();

	void setStatus(Status newStatus);
	
	void setMemcachedNode(MemcachedNode node );
	
	void readReplyFromBuffer(ChannelBuffer buffer);
	

	// [ new ]...initialize.. [start send ] ...send cmd ... [send end ]
	// ...read_queue....[start read ]...reading....[read end ]......complete
	static enum Status {
		INITIALIZE, SEND_CMD, READ_QUEUE, READ_MSG, COMPLETE, CANCELLE
	}
	
	

}
