package org.wzj.memcached.operation;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.wzj.memcached.node.MemcachedNode;


/**
 * 
 * @author Wen
 *
 */
public interface Operaction {

	boolean isCancelled();

	boolean isComplete();

	boolean cancel();


	void initialize() throws IOException;

	Status getStatus();

	void setStatus(Status newStatus);
	
	void setMemcachedNode(MemcachedNode node );
	
	void readReplyFromBuffer(ChannelBuffer buffer);
	

	// [ new ]...initialize.. [start send ] ...send cmd ... [send end ]
	// ...read_queue....[start read ]...reading....[read end ]......complete
	public static enum Status {
		INITIALIZE, SEND_CMD, READ_QUEUE, READ_MSG, COMPLETE, CANCELLE
	}
	
	

}
