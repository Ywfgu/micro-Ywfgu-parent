package com.ltyc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2019/11/13
 */
@Component
public class NettyClient implements Runnable {

    @Override
    public void run() {
        System.out.println(">>>>>>>>>>>>>>>>start<<<<<<<<<<<<<<<<<<");
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group);
            b.channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
//                    pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                    pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
//                    pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
//                    pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                    pipeline.addLast("FrameDecoder", new LengthFieldBasedFrameDecoder(4 * 1024 , 0, 4, -4, 0, true));
                    pipeline.addLast("HeaderCodec", new HeaderCodec());
                    pipeline.addLast("sessionLogin", new SessionLoginManager());
                    pipeline.addLast("handler", new HelloClient());
                }
            });
//            for (int i = 0; i < 100000; i++) {
                ChannelFuture f = b.connect("127.0.0.1", 7891).sync();
//                f.channel().writeAndFlush("hello Service!"+Thread.currentThread().getName()+":--->:"+i);
                f.channel().closeFuture().sync();
//            }


        } catch (Exception e) {

        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
//        for (int i = 0; i < 1000; i++) {
            new Thread(new NettyClient()).start();
//        }
    }

}
