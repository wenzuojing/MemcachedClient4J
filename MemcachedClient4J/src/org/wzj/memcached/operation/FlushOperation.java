package org.wzj.memcached.operation;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.wzj.memcached.MemcachedConstants;

public class FlushOperation extends BaseOperation {

	public FlushOperation(Callback callback) {
		super(MemcachedConstants.CMD_FLUSH, callback);
	}

	@Override
	public void initialize() throws IOException {

		ChannelFuture writeFuture = node.getChannel().write(
				ChannelBuffers.wrappedBuffer("flush_all\r\n"
						.getBytes(MemcachedConstants.DEFAULT_CHARSET)));

		writeFuture.addListener(new WriteCompleteListener());

	}

	@Override
	public void readReplyFromBuffer(ChannelBuffer buffer) {

		if (buffer.readable()) {
			String line = line(buffer);
			if (line != null) {
				this.setStatus(Status.COMPLETE);
				if (line.equals(MemcachedConstants.OK)) {
					callback.complete(Boolean.TRUE);
				} else {
					callback.complete(Boolean.FALSE);
				}

			}
		}

	}

}
