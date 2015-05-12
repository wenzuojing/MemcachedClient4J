package org.wzj.memcached.future;

import org.wzj.memcached.MemcachedException;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author Wen
 */
public class OperationFuture<T> implements Future<T> {

    private  volatile T result ;
    private volatile boolean done = false ;


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {
        return done ;
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
            while ( !done) {
                wait(unit.toMillis(timeout));
            }
        } catch (InterruptedException e) {
            //
        }

        return result ;
    }

    public synchronized void setResult(T result) {
        this.result = result ;
        this.done = true ;
        notify();
    }

}
