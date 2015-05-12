package org.wzj.memcached;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wzj.memcached.future.OperationFuture;
import org.wzj.memcached.node.MemcachedNode;
import org.wzj.memcached.node.MemcachedNodeFactory;
import org.wzj.memcached.operation.OperationFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * @author Wen
 */
public class MemcachedClient {

    private static final Logger LOG = LoggerFactory.getLogger(MemcachedClient.class);

    private OperationFactory oFactory;
    private MemcachedNodeFactory memcachedNodeFactory;

    public MemcachedClient(String server, int connSize) {
        this(new String[]{server}, connSize);
    }

    public MemcachedClient(String[] servers, int connSize) {
        memcachedNodeFactory = new MemcachedNodeFactory(servers, connSize);
        oFactory = new OperationFactory(memcachedNodeFactory);
    }

    /**
     * Sync set data to memcached server .
     *
     * @param key   key to store cache under
     * @param value value object to cache
     * @return true/false indicating success
     */
    public boolean set(String key, Object value) {
        return this.set(key, value, null);
    }

    /**
     * Sync set data to memcached server .
     *
     * @param key    key to store cache under
     * @param value  value object to cache
     * @param expiry expiration
     * @return true/false indicating success
     */
    public boolean set(String key, Object value, Date expiry) {

        OperationFuture<Boolean> future = this.asynStore(MemcachedConstants.CMD_SET,
                key, value, expiry);
        Boolean result = Boolean.FALSE;
        try {
            result = future.get();

        } catch (Exception e) {
            LOG.error("fail to get the result ", e);
        }

        return result == null ? false : result;

    }

    /**
     * Async set data to memcached server .
     *
     * @param key   key to store cache under
     * @param value value object to cache
     * @return future object
     */
    public OperationFuture<Boolean> asynSet(String key, Object value) {
        return this.asynStore(MemcachedConstants.CMD_SET, key, value, null);
    }

    /**
     * Async set data to memcached server .
     *
     * @param key    key to store cache under
     * @param value  value object to cache
     * @param expiry expiration
     * @return future object
     */
    public OperationFuture<Boolean> asynSet(String key, Object value, Date expiry) {
        return this.asynStore(MemcachedConstants.CMD_SET, key, value, expiry);
    }

    /**
     * Sync add data to the server .
     *
     * @param key   key to store cache under
     * @param value
     * @return true/false indicating success
     */
    public boolean add(String key, Object value) {
        return this.add(key, value, null);
    }

    /**
     * Sync add data to the server .
     *
     * @param key    key to store cache under
     * @param value  value object to cache
     * @param expiry expiration
     * @return true/false indicating success
     */
    public boolean add(String key, Object value, Date expiry) {
        Boolean result = Boolean.FALSE;
        OperationFuture<Boolean> future = this.asynAdd(key, value, expiry);
        try {
            result = future.get();
        } catch (Exception e) {
            LOG.error("fail to get the result ", e);
        }

        return result == null ? false : result;
    }

    /**
     * Async add data to memcached server .
     *
     * @param key   key to store cache under
     * @param value value object to cache
     * @return future object
     */
    public OperationFuture<Boolean> asynAdd(String key, Object value) {
        return this.asynStore(MemcachedConstants.CMD_ADD, key, value, null);
    }

    /**
     * Async add data to memcached server .
     *
     * @param key    key to store cache under
     * @param value  value object to cache
     * @param expiry expiration
     * @return future object
     */
    public OperationFuture<Boolean> asynAdd(String key, Object value, Date expiry) {
        return this.asynStore(MemcachedConstants.CMD_ADD, key, value, expiry);
    }

    /**
     * Sync replace data to the server .
     *
     * @param key   key to store cache under
     * @param value
     * @return true/false indicating success
     */
    public boolean replace(String key, Object value) {
        return this.replace(key, value, null);
    }

    /**
     * Sync replace data to the server .
     *
     * @param key    key to store cache under
     * @param value  value object to cache
     * @param expiry expiration
     * @return true/false indicating success
     */
    public boolean replace(String key, Object value, Date expiry) {
        Boolean result = Boolean.FALSE;
        OperationFuture<Boolean> future = this.asynReplace(key, value, expiry);
        try {
            result = future.get();
        } catch (Exception e) {
            LOG.error("fail to get the result ", e);
        }

        return result == null ? false : result;
    }

    /**
     * Async replace data to memcached server .
     *
     * @param key   key to store cache under
     * @param value value object to cache
     * @return future object
     */
    public OperationFuture<Boolean> asynReplace(String key, Object value) {

        return this.asynStore(MemcachedConstants.CMD_REPLACE, key, value, null);
    }

    /**
     * Async replace data to memcached server .
     *
     * @param key    key to store cache under
     * @param value  value object to cache
     * @param expiry expiration
     * @return future object
     */
    public OperationFuture<Boolean> asynReplace(String key, Object value, Date expiry) {
        return this.asynStore(MemcachedConstants.CMD_REPLACE, key, value, expiry);
    }

    /**
     * Sync replace data to the server .
     *
     * @param key   key to store cache under
     * @param value
     * @return true/false indicating success
     */
    public boolean append(String key, Object value) {
        return this.append(key, value, null);
    }

    /**
     * Sync append data to the server .
     *
     * @param key    key to store cache under
     * @param value  value object to cache
     * @param expiry expiration
     * @return true/false indicating success
     */
    public boolean append(String key, Object value, Date expiry) {
        Boolean result = Boolean.FALSE;
        OperationFuture<Boolean> future = this.asynAppend(key, value, expiry);
        try {
            result = future.get();
        } catch (Exception e) {
            LOG.error("fail to get the result ", e);
        }
        return result == null ? false : result;
    }

    /**
     * Async append data to memcached server .
     *
     * @param key   key to store cache under
     * @param value value object to cache
     * @return future object
     */
    public OperationFuture<Boolean> asynAppend(String key, Object value) {

        return this.asynStore(MemcachedConstants.CMD_APPEND, key, value, null);
    }

    /**
     * Async append data to memcached server .
     *
     * @param key    key to store cache under
     * @param value  value object to cache
     * @param expiry expiration
     * @return future object
     */
    public OperationFuture<Boolean> asynAppend(String key, Object value, Date expiry) {

        return this.asynStore(MemcachedConstants.CMD_APPEND, key, value, null);
    }

    /**
     * Sync retrieves stats specified server .
     *
     * @param server specified server ,example 127.0.0.1:11211
     * @return stats map , it will be null , when operate timeout or fail to get
     * the result
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> stats(String server) {
        Map<String, String> result = null;
        OperationFuture<Map<String, String>> future = this.asynStats(server);
        try {
            result = future.get();
        } catch (Exception e) {
            LOG.error("fail to get the result ", e);
        }

        return result;
    }

    /**
     * Async retrieves stats specified server .
     *
     * @param server
     * @return future object
     */
    public OperationFuture<Map<String, String>> asynStats(String server) {
        return oFactory.createStats(server).handle();
    }

    /**
     * Sync invalidate the entire cache.
     *
     * @param server specified server ,example 127.0.0.1:11211
     * @return true/false indicating success
     */
    public boolean flush(String server) {
        Boolean result = Boolean.FALSE;
        OperationFuture<Boolean> future = this.asynFlush(server);
        try {
            result = future.get();
        } catch (Exception e) {
            LOG.error("fail to get the result ", e);
        }

        return result;
    }

    /**
     * Async invalidate the entire cache.
     *
     * @param server specified server ,example 127.0.0.1:11211
     * @return future object
     */
    public OperationFuture<Boolean> asynFlush(String server) {
        return oFactory.createFlush(server).handle();
    }

    /**
     * Sync retrieve a key from the server
     *
     * @param key key where data is stored
     * @return the object that was previously stored, or null if it was not
     * previously stored
     */
    public Object get(String key) {
        return this.gets(new String[]{key}).get(key);
    }

    /**
     * Sync retrieve muti key from the server
     *
     * @param keys keys where data is stored
     * @return the map object that was previously stored
     */
    public Map<String, Object> gets(String... keys) {
        Map<String, Object> result = null;
        OperationFuture<Map<String, Object>> future = this.asynGets(keys);
        try {
            result = future.get();
        } catch (Exception e) {
            LOG.error("fail to get the result ", e);
        }

        return result;
    }

    /**
     * Async retrieve muti key from the server
     *
     * @param key key where data is stored
     * @return future object
     */
    public OperationFuture<Map<String, Object>> asynGet(String key) {
        return this.asynGet(new String[]{key});
    }

    /**
     * Async retrieve muti key from the server
     *
     * @param keys keys where data is stored
     * @return future object
     */
    public OperationFuture<Map<String, Object>> asynGets(String... keys) {
        return this.asynGet(keys);
    }

    /**
     * Delete an object from cache given cache key
     *
     * @param key the key to be removed
     * @return <code>true</code>, if the data was deleted successfully
     */
    public boolean delete(String key) {
        return this.delete(key, null);
    }

    /**
     * Delete an object from cache given cache key
     *
     * @param key    the key to be removed
     * @param expiry when to expire the record
     * @return <code>true</code>, if the data was deleted successfully
     */
    public boolean delete(String key, Date expiry) {
        Boolean result = Boolean.FALSE;
        OperationFuture<Boolean> future = this.asynDelete(key, expiry);
        try {
            result = future.get();
        } catch (Exception e) {
            LOG.error("fail to get the result ", e);
        }

        return result;
    }

    /**
     * Asyn delete an object from cache given cache key
     *
     * @param key the key to be removed
     * @return future object
     */
    public OperationFuture<Boolean> asynDelete(String key) {
        return this.asynDelete(key, null);
    }

    /**
     * Asyn delete an object from cache given cache key
     *
     * @param key    the key to be removed
     * @param expiry when to expire the record
     * @return future object
     */
    public OperationFuture<Boolean> asynDelete(String key, Date expiry) {
        return oFactory.createDelete(key, expiry).handle();
    }

    /**
     * Increment the value at the specified key by 1
     *
     * @param key key where the data is stored
     * @return -1, if the key is not found, the value after incrementing otherwise
     */
    public long incr(String key) {
        return this.incr(key, 1L);
    }

    /**
     * Increment the value at the specified key by passed in val.
     *
     * @param key key where the data is stored
     * @param inc how much to increment by
     * @return -1, if the key is not found, the value after incrementing otherwise
     */
    public long incr(String key, long inc) {
        Long result = Long.valueOf(-1);
        OperationFuture<Long> future = this.asynIncr(key, inc);

        try {
            result = future.get();
        } catch (Exception e) {
            LOG.error("fail to get the result ", e);
        }

        return result;
    }

    /**
     * Increment the value at the specified key by 1
     *
     * @param key key where the data is stored
     * @return future object
     */
    public OperationFuture<Long> asynIncr(String key) {
        return asynIncr(key, 1L);
    }

    /**
     * Increment the value at the specified key by passed in value.
     *
     * @param key key where the data is stored
     * @param inc how much to increment by
     * @return future object
     */
    public OperationFuture<Long> asynIncr(String key, long inc) {
        return asyncIncrOrDecr(MemcachedConstants.CMD_INCR, key, inc);
    }

    /**
     * Decrement the value at the specified key by 1
     *
     * @param key key where the data is stored
     * @return -1, if the key is not found, the value after incrementing otherwise
     */
    public long decr(String key) {
        return this.decr(key, 1L);
    }

    /**
     * Decrement the value at the specified key by passed in value
     *
     * @param key key where the data is stored
     * @param inc how much to decrement by
     * @return -1, if the key is not found, the value after incrementing otherwise
     */
    public long decr(String key, long inc) {
        Long result = Long.valueOf(-1);
        OperationFuture<Long> future = this.asynDecr(key, inc);
        try {
            result = future.get();
        } catch (Exception e) {
            LOG.error("fail to get the result ", e);
        }

        return result;
    }

    /**
     * Decrement the value at the specified key by 1
     *
     * @param key key where the data is stored
     * @return future object
     */
    public OperationFuture<Long> asynDecr(String key) {
        return asynDecr(key, 1L);
    }

    /**
     * Decrement the value at the specified key by passed in value
     *
     * @param key key where the data is stored
     * @param inc how much to decrement by
     * @return future object
     */
    public OperationFuture<Long> asynDecr(String key, long inc) {
        return asyncIncrOrDecr(MemcachedConstants.CMD_DECR, key, inc);
    }

    private OperationFuture<Boolean> asynStore(String cmd, String key, Object value, Date expiry) {
        return oFactory.createStore(cmd, key, value, expiry).handle();
    }

    private OperationFuture<Map<String, Object>> asynGet(String[] keys) {
        return oFactory.createGet(keys).handle();
    }

    private OperationFuture<Long> asyncIncrOrDecr(String cmd, String key, long inc) {
        return oFactory.createIncrOrDecr(cmd, key, inc).handle();
    }

    /**
     * release resource
     */
    public void shutdown() {
        Collection<MemcachedNode> allMemcachedNodes = memcachedNodeFactory.getAllMemcachedNodes();
        Throwable throwable = null;
        for (MemcachedNode node : allMemcachedNodes) {
            try {
                node.getConnction().shutdown();
            } catch (IOException e) {
                throwable = e;
            }
        }

        if (throwable != null) {
            throw new MemcachedException(throwable);
        }
    }

}
