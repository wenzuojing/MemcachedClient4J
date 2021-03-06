package org.wzj.memcached.operation;

import org.wzj.memcached.MemcachedConstants;

/**
 * @author Wen
 */
public class AddOperation extends StoreOperation {

    public AddOperation(String key, Object value, int expiry) {
        super(MemcachedConstants.CMD_ADD, key, value, expiry);
    }

}
