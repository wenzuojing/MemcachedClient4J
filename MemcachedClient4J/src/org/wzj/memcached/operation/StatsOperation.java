package org.wzj.memcached.operation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.wzj.memcached.MemcachedConstants;

public class StatsOperation  extends BaseOperation {
	
	private Map<String , String > stats  = new HashMap<String, String>() ;

	public StatsOperation( Callback callback) {
		super(MemcachedConstants.CMD_STATS , callback ) ;
	}

	@Override
	public void initialize() throws IOException {
		
		ChannelFuture writeFuture = node.getChannel().write(ChannelBuffers.wrappedBuffer("stats\r\n".getBytes(MemcachedConstants.DEFAULT_CHARSET))) ;
		
		writeFuture.addListener(new WriteCompleteListener());
		
		
	}

	@Override
	public void readReplyFromBuffer(ChannelBuffer buffer) {
		
		if(buffer.readable()){
			String line  = line(buffer);
			
			if(line != null ){
				
				if(line.startsWith(MemcachedConstants.STATS)){
					
					String[] split = line.split(" ");
					
					stats.put(split[1], split[2]) ;
					
					readReplyFromBuffer(buffer) ;
					
				}else if(line.equals(MemcachedConstants.END)){
					
					this.setStatus(Status.COMPLETE);
					
					callback.complete(stats);
					
				}else{
					readReplyFromBuffer(buffer) ;
				}
				
			}
		}
		
		
	}

}
