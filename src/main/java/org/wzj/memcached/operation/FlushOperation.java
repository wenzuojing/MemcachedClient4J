package org.wzj.memcached.operation;

import org.wzj.memcached.MemcachedConstants;

public class FlushOperation extends BaseOperation {

    public FlushOperation() {
        super(MemcachedConstants.CMD_FLUSH);
    }


    @Override
    public byte[] buildCmd() {
        return "flush_all\r\n".getBytes(MemcachedConstants.DEFAULT_CHARSET);
    }

    @Override
    protected void reply(String reply) {
        if (reply.equals(MemcachedConstants.OK)) {
            this.setResult(Boolean.TRUE);
        } else {
            this.setResult(Boolean.FALSE);
        }
    }

}
