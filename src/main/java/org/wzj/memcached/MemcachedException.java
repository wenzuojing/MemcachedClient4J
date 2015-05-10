package org.wzj.memcached;

/**
 * Created by wens on 15-5-8.
 */
public class MemcachedException extends RuntimeException {
    public MemcachedException() {
    }

    public MemcachedException(String message) {
        super(message);
    }

    public MemcachedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemcachedException(Throwable cause) {
        super(cause);
    }

    public MemcachedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
