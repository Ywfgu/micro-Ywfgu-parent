package com.ltyc.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/19
 */
public class TimeServerHandlerAdapter extends ChannelInboundHandlerAdapter {
    private AtomicLong cnt = new AtomicLong();
    private long lastNum = 0;
    private static final Logger logger = LoggerFactory.getLogger(TimeServerHandlerAdapter.class);
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        new Thread(new Runnable(){
            @Override
            public void run() {
                while(true){
                    long nowcnt = cnt.get();
                    logger.info("channels : ?,Totle Receive Msg Num:{},   speed : {}/s",
                            nowcnt, (nowcnt - lastNum) / 1.0);
                    lastNum = nowcnt;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ChannelFuture future = reponse(ctx);
        if (future != null) {
            future.addListener(new GenericFutureListener() {
                @Override
                public void operationComplete(Future future) throws Exception {
                    cnt.incrementAndGet();
                }
            });
        }
    }

    private ChannelFuture reponse(ChannelHandlerContext ctx) {
        final ByteBuf time = ctx.alloc().buffer(4);
        time.writeLong(System.currentTimeMillis());

        // write data
        final ChannelFuture f = ctx.writeAndFlush(time);
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                assert f == future;
                ctx.close();
            }
        });
        return f;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
