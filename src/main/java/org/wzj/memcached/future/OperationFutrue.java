package org.wzj.memcached.future;

import org.wzj.memcached.MemcachedException;
import org.wzj.memcached.operation.Operaction;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author Wen
 */
public class OperationFutrue<T> implements Future<T> {

    private Operaction operation;

    private AtomicReference<T> msg = new AtomicReference<T>();

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return operation.cancel();
    }

    @Override
    public boolean isCancelled() {
        return operation.isCancelled();
    }

    @Override
    public boolean isDone() {
        return operation.isComplete();
    }

    @Override
    public T get() {
        try {
            return this.get(60, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw new MemcachedException("Timeout", e);
        }
    }

    @Override
    public synchronized T get(long timeout, TimeUnit unit) throws TimeoutException {
        try {
            wait(unit.toMillis(timeout));
        } catch (InterruptedException e) {
            //
        }
        return this.msg.get();
    }

    public Operaction getOperation() {
        return operation;
    }

    public void setOperation(Operaction operation) {
        this.operation = operation;
    }

    public synchronized void setMsg(T msg) {
        this.msg.set(msg);
        notify();
    }

}
