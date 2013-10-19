package org.wzj.memcached;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

/**
 * 
 * @author Wen
 *
 */
public abstract class StoreOperation extends BaseOperation {
	
	protected String key ;
	
	protected int expiry ;
	
	protected Object value ;

	public StoreOperation(String cmd, String key, Object value , int expiry ,Callback callback) {
		super(cmd ,callback );
		this.key = key;
		this.value = value ;
		this.expiry = expiry;
	}



	@Override
	public void initialize() {
		
		StringBuffer sb  = new StringBuffer(64) ;
		
		sb.append(cmd).append(" ")//cmd
		.append(key).append(" ") ;//key
		
		CachedData cachedData = TranscoderFactory.getTranscoder().encode(value);
		
		sb.append(cachedData.getFlags()).append(" ")//flags
		.append(expiry).append(" ")//expiry
		.append(cachedData.getData().length)//bytes
		.append("\r\n");
		
		
		if( this.status.compareAndSet(Status.INITIALIZE, Status.READ_QUEUE)){
			Channel channel  =node.getChannel() ;
			channel.write(sb.toString()) ; //send cmd 
			
			channel.write(ChannelBuffers.wrappedBuffer(cachedData.getData())) ;
			ChannelFuture write = channel.write("\r\n") ;
			write.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					ReplyHandler reply =  (ReplyHandler)future.getChannel().getPipeline().get("reply") ;
					reply.enqueue(StoreOperation.this) ; 
					
				}
			}) ;
		}
		
		
	}
	
	
	@Override
	public void readReplyFromBuffer(ChannelBuffer buffer) {
		
		String line = line(buffer);
		if(line != null ){
			this.setStatus(Status.COMPLETE);
			if(line.equals(MemcachedConstants.STORED)){
				callback.complete(Boolean.TRUE) ;
			}else if(line.equals(MemcachedConstants.NOTSTORED)){
				callback.complete(Boolean.FALSE) ;
			}else{
				throw new RuntimeException("server error" ) ;
			}
		}
		
	}

}
