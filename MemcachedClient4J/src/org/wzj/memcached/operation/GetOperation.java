package org.wzj.memcached.operation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.wzj.memcached.CachedData;
import org.wzj.memcached.MemcachedConstants;
import org.wzj.memcached.transcoder.TranscoderFactory;

public class GetOperation extends BaseOperation {

	private String[] keys;

	private String[] dataInfo = null;

	private Map<String, Object> dataMap = new HashMap<String, Object>();

	public GetOperation(String[] keys, Callback callback) {
		super(MemcachedConstants.CMD_GET, callback);
		this.keys = keys;
	}

	@Override
	public void initialize() throws IOException {

		StringBuilder sb = new StringBuilder(64);

		sb.append("get");

		for (String key : keys) {
			sb.append(" ").append(key);
		}

		sb.append("\r\n");

		ChannelFuture writeFuture = node.getChannel().write(
				ChannelBuffers.wrappedBuffer(sb.toString().getBytes(
						MemcachedConstants.DEFAULT_CHARSET)));

		writeFuture.addListener(new WriteCompleteListener());

	}

	@Override
	public void readReplyFromBuffer(ChannelBuffer buffer) {

		if (buffer.readable()) {
			if (dataInfo != null) {

				int dataLength = Integer.parseInt(dataInfo[3]);

				if (buffer.readableBytes() >= (dataLength + 2 /* two end char */)) {

					byte[] dd = new byte[dataLength];

					buffer.readBytes(dd);
					buffer.readBytes(new byte[2]);

					CachedData cachedData = new CachedData(
							Integer.parseInt(dataInfo[2]), dd);

					Object obj = TranscoderFactory.getTranscoder().decode(
							cachedData);

					dataMap.put(dataInfo[1], obj);

					dataInfo = null;
					
					readReplyFromBuffer(buffer);
				}

			} else {
				String line = line(buffer);

				if (line != null) {

					if (line.startsWith(MemcachedConstants.VALUE)) {

						dataInfo = line.split(" "); // example : VALUE key 32 10
						
						readReplyFromBuffer(buffer);

					} else if (line.startsWith(MemcachedConstants.END)) {
						this.setStatus(Status.COMPLETE);
						callback.complete(dataMap);
					} else {
						throw new RuntimeException("server error");
					}

				}
				
			}

			
		}

	}

}
