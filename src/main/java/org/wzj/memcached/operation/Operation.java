package org.wzj.memcached.operation;


import org.wzj.memcached.future.OperationFuture;
import org.wzj.memcached.node.MemcachedNode;


/**
 * @author Wen
 */
public interface Operation<T> extends TextProtocol {

    boolean isComplete();

    void setStatus(Status status);

    void setMemcachedNode(MemcachedNode node);

    OperationFuture<T> handle();

    enum Status {
        INITIALIZE, COMPLETE
    }


}
