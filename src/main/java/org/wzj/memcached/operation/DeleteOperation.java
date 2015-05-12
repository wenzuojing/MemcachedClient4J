package org.wzj.memcached.operation;

import org.wzj.memcached.MemcachedConstants;

import java.util.Date;

public class DeleteOperation extends BaseOperation {

    private String key;

    private Date expiry;

    public DeleteOperation(String key, Date expiry) {
        super(MemcachedConstants.CMD_DELETE);
        this.key = key;
        this.expiry = expiry;
    }


    @Override
    public byte[] buildCmd() {
        StringBuilder sb = new StringBuilder("delete ");
        sb.append(key);
        if (expiry != null) {
            sb.append(" ").append(expiry.getTime() / 1000);
        }
        sb.append("\r\n");
        return sb.toString().getBytes(MemcachedConstants.DEFAULT_CHARSET);
    }

    @Override
    protected void reply(String reply) {
        if (reply.equals(MemcachedConstants.DELETED)) {
            this.setResult(Boolean.TRUE);
        } else {
            this.setResult(Boolean.FALSE);
        }
    }

}
