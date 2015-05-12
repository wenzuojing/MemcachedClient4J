package org.wzj.memcached.operation;

import org.wzj.memcached.MemcachedException;
import org.wzj.memcached.future.OperationFuture;
import org.wzj.memcached.node.MemcachedNode;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author Wen
 */
public abstract class BaseOperation implements Operation {

    protected String cmd;
    protected AtomicReference<Status> status = new AtomicReference<Status>();
    protected MemcachedNode node;

    protected volatile Object result ;

    @Override
    public boolean cancel() {
        return  status.compareAndSet(Status.INITIALIZE , Status.CANCELED ) ;
    }

    @Override
    public boolean isCancelled() {
        return status.get() == Status.INITIALIZE;
    }

    /**
     * @param cmd
     */
    public BaseOperation(String cmd) {
        this.cmd = cmd;
        status.set(Status.INITIALIZE);
    }


    @Override
    public boolean isComplete() {
        if (status.get() == Status.COMPLETED) {
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
    public <T> OperationFuture<T> handle() {
        try {
            status.set(Status.HANDLING) ;
            OperationFuture<T> operationFuture = new OperationFuture<T>(this);
            node.getConnction().send(this);
            return operationFuture;
        } catch (IOException e) {
            throw new MemcachedException(e);
        }
    }

    @Override
    public  synchronized  <T> T waitingHandleResult(long timeout, TimeUnit unit) throws TimeoutException {

        long start = System.currentTimeMillis() ;
        timeout  = unit.toMillis(timeout) ;
        while (status.get() != Status.COMPLETED) {
            long ela = System.currentTimeMillis() - start;
            if (timeout - ela > 0) {
                try {
                    wait(timeout - ela);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                throw new TimeoutException("Timeout");
            }
        }
        return (T)result;
    }


    public synchronized void setResult(Object result) {
        if( this.status.compareAndSet(Status.HANDLING , Status.COMPLETED)  ){
            this.result = result;
                notify();
        }else{
            throw new IllegalStateException( "Expected status is "+Status.HANDLING+", but the reality is "+this.status.get().name()) ;
        }

    }
}
