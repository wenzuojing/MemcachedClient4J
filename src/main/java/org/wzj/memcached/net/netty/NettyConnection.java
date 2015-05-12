package org.wzj.memcached.net.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.wzj.memcached.MemcachedException;
import org.wzj.memcached.net.Connection;
import org.wzj.memcached.operation.Operation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


/**
 * @author Wen
 */
public class NettyConnection implements Connection {

    private String host;

    private int port;

    private List<Channel> chanels;

    private Bootstrap bootstrap;

    private int connSize;

    private AtomicLong counter;


    public NettyConnection(String host, int port, int connSize) {
        this.chanels = new ArrayList<Channel>(connSize);
        this.connSize = connSize;
        this.host = host;
        this.port = port;
        counter = new AtomicLong(0);
        EventLoopGroup group = new NioEventLoopGroup(connSize);
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        //p.addLast(new LoggingHandler(LogLevel.INFO));
                        p.addLast(new ConnectHandler(new Listener()));
                        p.addLast(new OperationEncoder());
                        p.addLast(new ReplyHandler());

                    }
                });

        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, false);


    }

    public void shutdown() throws IOException {
        bootstrap.group().shutdownGracefully();
    }


    @Override
    public void connect() throws IOException {
        for (int i = 0; i < connSize; i++) {
            bootstrap.connect(host, port);
        }
    }

    @Override
    public boolean isConnected() {
        return getChanels().size() > 0;
    }

    @Override
    public void send(Operation operation) throws IOException {
        doSend(operation, 5);
    }

    private void doSend(Operation operation, int retry) {
        if (retry > 5) {
            throw new MemcachedException("No connections available");
        }

        Channel channel = null;

        for (int i = 0; i < retry; i++) {
            long l = counter.incrementAndGet();
            List<Channel> chanels = getChanels();
            if (chanels.size() == 0) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
                continue;
            } else {
                channel = chanels.get((int) (l % chanels.size()));
                break;
            }
        }

        if (channel != null) {
            channel.writeAndFlush(operation);
        } else {
            throw new MemcachedException("No connections available for:" + host + ":" + port);
        }
    }

    public synchronized List<Channel> getChanels() {
        return chanels;
    }

    public synchronized void addChanel(Channel channel) {
        if (!chanels.contains(channel)) {
            chanels.add(channel);
        }
    }

    public synchronized void removeChanel(Channel channel) {
        chanels.remove(channel);
    }

    private class Listener implements ConnectListener {
        @Override
        public void onConnected(Channel channel) {
            addChanel(channel);
        }

        @Override
        public void onDisconnected(Channel channel) {
            removeChanel(channel);
        }

        @Override
        public void onExceptionCaught(Channel channel) {
            removeChanel(channel);
        }

    }


}
