package org.wzj.memcached;


import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.string.StringEncoder;

/**
 * 
 * @author Wen
 *
 */
public class MemcachedChannelPipelineFactory  implements ChannelPipelineFactory {

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		
		ChannelPipeline pipeline = Channels.pipeline();
		
		//pipeline.addLast("stringEncoder", new StringEncoder(MemcachedConstants.DEFAULT_CHARSET)) ;
		
		pipeline.addLast("reply", new ReplyHandler()) ;
		
		return pipeline;
	}

}
