package org.wzj.memcached.net;

import org.wzj.memcached.operation.Operation;

import java.io.IOException;

/**
 * Created by wens on 15-5-8.
 */
public interface Connection {

    void connect() throws IOException;

    boolean isConnected();

    void send(Operation operaction) throws IOException;

    void shutdown() throws IOException;


}
