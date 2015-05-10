package org.wzj.memcached.net.netty;

import org.junit.Test;
import org.wzj.memcached.operation.FlushOperation;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by wens on 15-5-8.
 */
public class NettyConnectTest {

    @Test
    public void test_0() throws InterruptedException, IOException {
        NettyConnection connection = new NettyConnection("localhost", 11211, 1 );

        connection.connect();
        TimeUnit.SECONDS.sleep(1);

        System.out.println(connection.isConnected());

        connection.send(new FlushOperation(null)) ;

        TimeUnit.HOURS.sleep(1);

    }
}
