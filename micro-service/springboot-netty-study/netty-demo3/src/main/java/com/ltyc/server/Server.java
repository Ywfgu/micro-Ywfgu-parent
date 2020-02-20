package com.ltyc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/19
 */
public class Server {

    public static void main(String[] args) {
        new Server().bind(8080);
    }

    private void bind(int port) {
        EventLoopGroup boosGroup = new NioEventLoopGroup(1, newThreadFactory("bossGroup"));
        EventLoopGroup workerGroup = new NioEventLoopGroup(0, newThreadFactory("workGroup"));
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .childOption(ChannelOption.SO_RCVBUF, 2048)
                .childOption(ChannelOption.SO_SNDBUF, 2048)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(1024))
                .childOption(ChannelOption.TCP_NODELAY, true)
                .handler(new LoggingHandler(LogLevel.DEBUG));//.childHandler(initPipeLine());
                bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //流控
                        socketChannel.pipeline().addLast(new MessageChannelTrafficShapingHandler(200, 200, 250));
                        // bind
                        socketChannel.pipeline().addLast(new TimeServerHandlerAdapter());
                    }
                });

        try {
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class MessageChannelTrafficShapingHandler extends ChannelTrafficShapingHandler {
        public MessageChannelTrafficShapingHandler(long writeLimit, long readLimit, long checkInterval) {
            super(writeLimit, readLimit, checkInterval);
            // 积压75条,或者延迟超过2.5s就不能再写了
            setMaxWriteSize(75);
            setMaxWriteDelay(2500);
        }

        @Override
        protected long calculateSize(Object msg) {
            if (msg.toString().equals("request")) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private static ThreadFactory newThreadFactory(final String name){

        return new ThreadFactory() {

            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread( r,name + threadNumber.getAndIncrement());

                t.setDaemon(true);
                if (t.getPriority() != Thread.NORM_PRIORITY) {
                    t.setPriority(Thread.NORM_PRIORITY);
                }
                return t;
            }
        };

    }




}
