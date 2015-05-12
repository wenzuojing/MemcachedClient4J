package org.wzj.memcached.future;

import org.wzj.memcached.MemcachedException;
import org.wzj.memcached.operation.Operation;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * @author Wen
 */
public class OperationFuture<T> implements Future<T> {

    private Operation operation ;

    public OperationFuture(Operation operation){
        this.operation = operation ;

    }


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
        return operation.isComplete() ;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        try {
            return get( 600  ,TimeUnit.SECONDS );
        } catch (TimeoutException e) {
            throw new MemcachedException("Timeout" , e );
        }
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return operation.waitingHandleResult(timeout, unit);
    }
}
