package org.wzj.memcached.operation;


import org.wzj.memcached.node.MemcachedNode;


/**
 * @author Wen
 */
public interface Operation extends TextProtocol {

    boolean isCancelled();

    boolean isComplete();

    boolean cancel();

    void setStatus(Status status);

    void setMemcachedNode(MemcachedNode node);

    Opera handle();

    enum Status {
        INITIALIZE, COMPLETE, CANCELLE
    }


}
