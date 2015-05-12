package org.wzj.memcached.future;

import org.wzj.memcached.MemcachedException;
import org.wzj.memcached.operation.Operation;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author Wen
 */
public class OperationFuture<T> implements Future<T> {

    private AtomicReference<T> msg = new AtomicReference<T>();

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException() ;
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException() ;
    }

    @Override
    public boolean isDone() {
        return msg.get() == null ;
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
            while (this.msg.get() == null ){
                wait(unit.toMillis(timeout));
            }
        } catch (InterruptedException e) {
            //
        }

        return this.msg.get();
    }

    public synchronized void setMsg(T msg) {
        this.msg.set(msg);
        notify();
    }

}
