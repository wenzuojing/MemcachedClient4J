package org.wzj.memcached.operation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.wzj.memcached.CachedData;
import org.wzj.memcached.MemcachedConstants;
import org.wzj.memcached.transcoder.TranscoderFactory;

/**
 * 
 * @author Wen
 * 
 */
public abstract class StoreOperation extends BaseOperation {

	protected String key;

	protected int expiry;

	protected Object value;

	public StoreOperation(String cmd, String key, Object value, int expiry,
			Callback callback) {
		super(cmd, callback);
		this.key = key;
		this.value = value;
		this.expiry = expiry;
	}

	@Override
	public void initialize() throws IOException {
		ByteArrayOutputStream write = new ByteArrayOutputStream(64);
		StringBuffer sb = new StringBuffer(64);

		sb.append(cmd).append(" ")// cmd
				.append(key).append(" ");// key

		CachedData cachedData = TranscoderFactory.getTranscoder().encode(value);

		sb.append(cachedData.getFlags()).append(" ")// flags
				.append(expiry).append(" ")// expiry
				.append(cachedData.getData().length)// bytes
				.append("\r\n");

		if (this.status.compareAndSet(Status.INITIALIZE, Status.READ_QUEUE)) {

			Channel channel = node.getChannel();
			
			write.write(sb.toString().getBytes(
					MemcachedConstants.DEFAULT_CHARSET));
			write.write(cachedData.getData());
			write.write("\r\n".getBytes(MemcachedConstants.DEFAULT_CHARSET));
			ChannelFuture writeFuture = channel.write(ChannelBuffers
					.wrappedBuffer(write.toByteArray()));
			write.close();
			writeFuture.addListener(new WriteCompleteListener());
		}

	}

	@Override
	public void readReplyFromBuffer(ChannelBuffer buffer) {

		String line = line(buffer);
		if (line != null) {
			this.setStatus(Status.COMPLETE);
			if (line.equals(MemcachedConstants.STORED)) {
				callback.complete(Boolean.TRUE);
			} else if (line.equals(MemcachedConstants.NOTSTORED)) {
				callback.complete(Boolean.FALSE);
			} else { //error
				//throw new RuntimeException("server error");
				callback.complete(Boolean.FALSE);
			}
		}

	}

}
