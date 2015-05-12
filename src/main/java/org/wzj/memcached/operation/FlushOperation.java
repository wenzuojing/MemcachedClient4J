package org.wzj.memcached.operation;

import org.wzj.memcached.MemcachedConstants;

public class FlushOperation extends BaseOperation<Boolean> {

    public FlushOperation() {
        super(MemcachedConstants.CMD_FLUSH);
    }


    @Override
    public byte[] buildCmd() {
        return "flush_all\r\n".getBytes(MemcachedConstants.DEFAULT_CHARSET);
    }

    @Override
    protected void reply(String reply) {
        this.setStatus(Status.COMPLETE);
        if (reply.equals(MemcachedConstants.OK)) {
            operationFuture.setResult(Boolean.TRUE);
        } else {
            operationFuture.setResult(Boolean.FALSE);
        }
    }

}
