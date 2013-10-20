package org.wzj.memcached.operation;

import java.io.IOException;
import java.util.Date;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.wzj.memcached.MemcachedConstants;

public class DeleteOperation  extends BaseOperation {
	
	private String key  ;
	
	private Date expiry ;

	public DeleteOperation(String key, Date expiry, Callback callback) {
		super(MemcachedConstants.CMD_DELETE  , callback) ;
		this.key  = key ;
		this.expiry = expiry ;
	}

	@Override
	public void initialize() throws IOException {
		
		StringBuilder sb  = new StringBuilder("delete ");
		
		sb.append(key) ;
		
		if(expiry != null ){
			sb.append(" " ).append(expiry.getTime() / 1000 ) ;
		}
		
		sb.append("\r\n") ;
		
		ChannelFuture writeFuture = node.getChannel().write(ChannelBuffers.wrappedBuffer(sb.toString().getBytes(MemcachedConstants.DEFAULT_CHARSET))) ;
		
		writeFuture.addListener(new WriteCompleteListener());
		
	}

	@Override
	public void readReplyFromBuffer(ChannelBuffer buffer) {
		
		String line  = line(buffer ) ;
		
		if(line  != null ){
			this.setStatus(Status.COMPLETE);
			
			if(line.equals(MemcachedConstants.DELETED )){
				callback.complete(Boolean.TRUE);
			}else{
				callback.complete(Boolean.FALSE	);
			}
		}
		
	}

}
