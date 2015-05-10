package org.wzj.memcached.operation;

import org.wzj.memcached.CachedData;
import org.wzj.memcached.MemcachedConstants;
import org.wzj.memcached.MemcachedException;
import org.wzj.memcached.transcoder.TranscoderFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Wen
 */
public abstract class StoreOperation extends BaseOperation {

    protected String key;

    protected int expiry;

    protected Object value;

    public StoreOperation(String cmd, String key, Object value, int expiry, Callback callback) {
        super(cmd, callback);
        this.key = key;
        this.value = value;
        this.expiry = expiry;
    }

    @Override
    public byte[] buildCmd() {
        ByteArrayOutputStream write = new ByteArrayOutputStream(64);
        StringBuffer sb = new StringBuffer(64);

        sb.append(cmd).append(" ")// cmd
                .append(key).append(" ");// key

        CachedData cachedData = TranscoderFactory.getTranscoder().encode(value);

        sb.append(cachedData.getFlags()).append(" ")// flags
                .append(expiry).append(" ")// expiry
                .append(cachedData.getData().length)// bytes
                .append("\r\n");

        try {
            write.write(sb.toString().getBytes(
                    MemcachedConstants.DEFAULT_CHARSET));
            write.write(cachedData.getData());
            write.write("\r\n".getBytes(MemcachedConstants.DEFAULT_CHARSET));

            write.flush();
        } catch (IOException e) {
            throw new MemcachedException(e);
        }

        return write.toByteArray();
    }


    @Override
    protected void reply(String reply) {
        this.setStatus(Status.COMPLETE);
        if (reply.equals(MemcachedConstants.STORED)) {
            callback.complete(Boolean.TRUE);
        } else if (reply.equals(MemcachedConstants.NOTSTORED)) {
            callback.complete(Boolean.FALSE);
        } else { //error
            callback.complete(Boolean.FALSE);
        }
    }


}
