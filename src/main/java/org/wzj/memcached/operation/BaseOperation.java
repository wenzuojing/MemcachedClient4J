package org.wzj.memcached.operation;

import org.wzj.memcached.MemcachedException;
import org.wzj.memcached.node.MemcachedNode;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author Wen
 */
public abstract class BaseOperation implements Operaction {

    protected String cmd;

    protected AtomicReference<Status> status = new AtomicReference<Status>();

    protected MemcachedNode node;

    protected Callback callback;


    /**
     * @param cmd
     */
    public BaseOperation(String cmd, Callback callback) {
        this.cmd = cmd;
        status.set(Status.INITIALIZE);
        this.callback = callback;
    }


    public Status getStatus() {
        return status.get();
    }

    public void setStatus(Status newStatus) {
        status.set(newStatus);
    }


    @Override
    public boolean isCancelled() {

        if (status.get() == Status.CANCELLE) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isComplete() {
        if (status.get() == Status.COMPLETE) {
            return true;
        }

        return false;
    }

    @Override
    public synchronized boolean cancel() {

        if (status.get() == Status.INITIALIZE) {
            status.set(Status.CANCELLE);
            return true;
        }

        return false;
    }


    public void setMemcachedNode(MemcachedNode node) {
        this.node = node;
    }

    public interface Callback {
        void complete(Object msg);
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
    public void handle() {
        try {
            node.getConnction().send(this);
        } catch (IOException e) {
            throw new MemcachedException(e);
        }
    }
}
