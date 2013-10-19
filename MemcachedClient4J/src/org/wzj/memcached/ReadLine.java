package org.wzj.memcached;

import java.io.ByteArrayOutputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * 
 * @author Wen
 *
 */
public class ReadLine {
	
	//private  ChannelBuffer   LFuffer   ;
	private ByteArrayOutputStream out   = new ByteArrayOutputStream(64) ;
	
	private boolean cr  = false  ;
	
	protected String line(ChannelBuffer buffer ){
		
		if(buffer.readable()){
			
			while(buffer.readable()){
				
				byte b = buffer.readByte() ;
				
				if(b == '\n' && cr ){
					cr = false ;
					
					
					
					String temp = new String(out.toByteArray() ,MemcachedConstants.DEFAULT_CHARSET ) ;  // LFuffer.toString(MemcachedConstants.DEFAULT_CHARSET) ;
					
					out.reset();
					
					return temp ;
					
				}
				
				if(b == '\r' ){
					cr = true ;
					continue ;
				}
				
				out.write(b);
			}
			
		}
		
		return null ;
		
	}

}
