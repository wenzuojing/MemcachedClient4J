package org.wzj.memcached.node;

import org.wzj.memcached.net.Connection;
import org.wzj.memcached.net.netty.NettyConnection;


/**
 * @author Wen
 */
public class MemcachedNode {

    private String host;
    private int port;

    private Connection connection;

    public MemcachedNode(String host, int port, int connSize) {
        this.host = host;
        this.port = port;
        connection = new NettyConnection(host, port, connSize);
        connection.connect();
    }

    public Connection getConnction() {
        return connection;
    }

    public String getServerInfo() {
        return host + ":" + port;
    }
}
