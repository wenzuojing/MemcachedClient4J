package org.wzj.memcached;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * 
 * @author Wen
 *
 */
public class ReadLine {
	
	private  ChannelBuffer   LFuffer  = ChannelBuffers.dynamicBuffer(64)  ;
	
	private boolean cr  = false  ;
	
	protected String line(ChannelBuffer buffer ){
		
		if(buffer.readable()){
			
			while(buffer.readable()){
				
				byte b = buffer.readByte() ;
				
				if(b == '\n' && cr ){
					cr = false ;
					
					String temp = LFuffer.toString(MemcachedConstants.DEFAULT_CHARSET) ;
					
					LFuffer.clear() ;
					
					return temp ;
					
				}
				
				if(b == '\r' ){
					cr = true ;
					continue ;
				}
				
				LFuffer.writeByte(b) ;
				
				//sb.append(b) ;
			}
			
		}
		
		return null ;
		
	}

}
