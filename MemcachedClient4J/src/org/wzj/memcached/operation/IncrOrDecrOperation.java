package org.wzj.memcached.operation;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.wzj.memcached.MemcachedConstants;

public class IncrOrDecrOperation extends BaseOperation {

	private String key;

	private long inc;

	public IncrOrDecrOperation(String cmd, String key, long inc,
			Callback callback) {
		super(cmd, callback);
		this.key = key;
		this.inc = inc;

	}

	@Override
	public void initialize() throws IOException {

		String temp = String.format("%s %s %d\r\n", cmd, key, inc);

		ChannelFuture writeFuture = this.node.getChannel().write(
				ChannelBuffers.wrappedBuffer(temp
						.getBytes(MemcachedConstants.DEFAULT_CHARSET)));

		writeFuture.addListener(new WriteCompleteListener());

	}

	@Override
	public void readReplyFromBuffer(ChannelBuffer buffer) {

		String line = line(buffer);
		if (line != null) {
			this.setStatus(Status.COMPLETE);
			if (line.matches("\\d+")) {
				callback.complete(Long.valueOf(line));
			} else { // It will return -1 , when not found or error
				callback.complete(Long.valueOf(-1));
			}

		}

	}

}
