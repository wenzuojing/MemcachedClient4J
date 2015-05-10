package org.wzj.memcached.net;

import org.wzj.memcached.operation.Operaction;

import java.io.IOException;

/**
 * Created by wens on 15-5-8.
 */
public interface Connection {

    void connect();

    boolean isConnected();

    void send(Operaction operaction) throws IOException;

    void shutdown();


}
