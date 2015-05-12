package org.wzj.memcached.operation;

import org.wzj.memcached.MemcachedException;
import org.wzj.memcached.future.OperationFuture;
import org.wzj.memcached.node.MemcachedNode;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author Wen
 */
public abstract class BaseOperation<T> implements Operation<T> {

    protected String cmd;
    protected AtomicReference<Status> status = new AtomicReference<Status>();
    protected MemcachedNode node;

    protected OperationFuture<T> operationFuture;

    /**
     * @param cmd
     */
    public BaseOperation(String cmd) {
        this.cmd = cmd;
        status.set(Status.INITIALIZE);
    }


    public void setStatus(Status status) {
        this.status.set(status);
    }


    @Override
    public boolean isComplete() {
        if (status.get() == Status.COMPLETE) {
            return true;
        }

        return false;
    }


    public void setMemcachedNode(MemcachedNode node) {
        this.node = node;
    }

    @Override
    public void setReply(String reply) {
        reply(reply);
    }

    protected void reply(String reply) {

    }

    @Override
    public void setDataBlock(byte[] data) {

    }

    @Override
    public int getDataBlockLength() {
        return -1;
    }

    @Override
    public OperationFuture<T> handle() {
        try {
            operationFuture = new OperationFuture<>();
            node.getConnction().send(this);
            return operationFuture;
        } catch (IOException e) {
            throw new MemcachedException(e);
        }
    }
}
