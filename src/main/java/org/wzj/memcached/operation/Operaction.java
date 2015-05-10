package org.wzj.memcached.operation;


import org.wzj.memcached.node.MemcachedNode;


/**
 * @author Wen
 */
public interface Operaction extends TextProtocol {

    boolean isCancelled();

    boolean isComplete();

    boolean cancel();

    Status getStatus();

    void setStatus(Status newStatus);

    void setMemcachedNode(MemcachedNode node);

    void handle();


    // [ new ]...initialize.. [start send ] ...send cmd ... [send end ]
    // ...read_queue....[start read ]...reading....[read end ]......complete
    public static enum Status {
        INITIALIZE, SEND_CMD, READ_QUEUE, READ_MSG, COMPLETE, CANCELLE
    }


}
