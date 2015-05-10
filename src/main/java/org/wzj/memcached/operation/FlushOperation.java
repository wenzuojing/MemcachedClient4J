package org.wzj.memcached.operation;

import org.wzj.memcached.MemcachedConstants;

public class FlushOperation extends BaseOperation {

    public FlushOperation(Callback callback) {
        super(MemcachedConstants.CMD_FLUSH, callback);
    }


    @Override
    public byte[] buildCmd() {
        return "flush_all\r\n".getBytes(MemcachedConstants.DEFAULT_CHARSET);
    }

    @Override
    protected void reply(String reply) {
        this.setStatus(Status.COMPLETE);
        if (reply.equals(MemcachedConstants.OK)) {
            callback.complete(Boolean.TRUE);
        } else {
            callback.complete(Boolean.FALSE);
        }
    }

}
