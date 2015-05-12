package org.wzj.memcached.net.netty;

import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by wens on 15-5-8.
 */
public class NettyConnectTest {

    @Test
    public void test_0() throws InterruptedException, IOException {
        NettyConnection connection = new NettyConnection("localhost", 11211, 1);
        connection.connect();
        TimeUnit.SECONDS.sleep(1);
        Assert.assertEquals(true, connection.isConnected());
    }

    @Test
    public void test_1() throws InterruptedException, IOException {
        NettyConnection connection = new NettyConnection("localhost", 11211, 10);
        connection.connect();
        TimeUnit.SECONDS.sleep(1);
        Assert.assertEquals(true, connection.isConnected());
    }
}
