package org.wzj.memcached.operation;

import org.wzj.memcached.MemcachedConstants;

/**
 * @author Wen
 */
public class SetOperation extends StoreOperation {

    public SetOperation(String key, Object value, int expiry) {

        super(MemcachedConstants.CMD_SET, key, value, expiry);
    }


}
