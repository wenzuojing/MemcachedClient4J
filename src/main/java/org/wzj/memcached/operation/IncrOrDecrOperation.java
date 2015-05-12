package org.wzj.memcached.operation;

import org.wzj.memcached.MemcachedConstants;

public class IncrOrDecrOperation extends BaseOperation<Long> {

    private String key;

    private long inc;

    public IncrOrDecrOperation(String cmd, String key, long inc) {
        super(cmd);
        this.key = key;
        this.inc = inc;

    }


    @Override
    public byte[] buildCmd() {
        return String.format("%s %s %d\r\n", cmd, key, inc).getBytes(MemcachedConstants.DEFAULT_CHARSET);
    }

    @Override
    protected void reply(String reply) {
        this.setStatus(Status.COMPLETE);
        if (reply.matches("\\d+")) {
            operationFuture.setResult(Long.valueOf(reply));
        } else { // It will return -1 , when not found or error
            operationFuture.setResult(Long.valueOf(-1));
        }
    }

}
