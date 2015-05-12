package org.wzj.memcached.operation;

import org.wzj.memcached.MemcachedConstants;


/**
 * @author Wen
 */
public class ReplaceOperation extends StoreOperation {

    public ReplaceOperation(String key, Object value, int expiry) {
        super(MemcachedConstants.CMD_REPLACE, key, value, expiry);
    }
}
