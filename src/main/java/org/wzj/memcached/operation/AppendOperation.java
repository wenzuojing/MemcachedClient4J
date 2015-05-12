package org.wzj.memcached.operation;

import org.wzj.memcached.MemcachedConstants;

/**
 * @author Wen
 */
public class AppendOperation extends StoreOperation {

    public AppendOperation(String key, Object value, int expiry) {
        super(MemcachedConstants.CMD_APPEND, key, value, expiry);
    }
}
