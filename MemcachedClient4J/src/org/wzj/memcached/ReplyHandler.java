package org.wzj.memcached;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.wzj.memcached.Operaction.Status;

/**
 * 
 * @author Wen
 *
 */
public class ReplyHandler  extends SimpleChannelUpstreamHandler {
	
	
	private BlockingQueue<Operaction> oQueue  = new  LinkedBlockingQueue<Operaction>();
	
	private  Operaction cOperaction ;
	
	private ChannelBuffer cumulation ;
	
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		
		
		if(cOperaction == null || cOperaction.getStatus() == Status.COMPLETE ){
			cOperaction = oQueue.take() ;
		}
		
		
		
		ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
		
		if(cumulation == null  || !cumulation.readable() ){
			cumulation = buffer ;
		}else{
			cumulation = ChannelBuffers.wrappedBuffer(cumulation , buffer) ;
		}
		
		cOperaction.readReplyFromBuffer(cumulation) ;
		
		
	}
	
	public void enqueue(Operaction operation ){
		oQueue.offer(operation) ;
	}

}
