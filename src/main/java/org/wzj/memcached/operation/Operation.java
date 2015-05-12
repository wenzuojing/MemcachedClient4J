package org.wzj.memcached.operation;


import org.wzj.memcached.future.OperationFuture;
import org.wzj.memcached.node.MemcachedNode;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * @author Wen
 */
public interface Operation extends TextProtocol {

    boolean isComplete();

    void setMemcachedNode(MemcachedNode node);

    <T> OperationFuture<T> handle();

    boolean cancel();

    boolean isCancelled();

    <T> T waitingHandleResult(long timeout, TimeUnit unit) throws TimeoutException;

    enum Status {
        INITIALIZE, HANDLING , COMPLETED ,CANCELED
    }


}
