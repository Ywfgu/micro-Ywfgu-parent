package com.ltyc.sms.handler;

import com.ltyc.sms.common.GlobalConstance;
import com.ltyc.sms.connect.manager.EndpointEntity;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class MessageLogHandler extends ChannelDuplexHandler {
    private final Logger logger;
    private EndpointEntity entity;

    public MessageLogHandler(EndpointEntity entity) {
        this.entity = entity;
        if (entity != null) {
            logger = LoggerFactory.getLogger(String.format(GlobalConstance.loggerNamePrefix, entity.getId()));
        } else {
            logger = LoggerFactory.getLogger(MessageLogHandler.class);
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.warn("handlerAdded . {}", entity);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.warn("Connection close . {}", entity);
        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("Receive:{}", msg);
        ctx.fireChannelRead(msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        final Object finalmsg = msg;
        ctx.write(msg, promise);
        promise.addListener(new GenericFutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                // 如果发送消息失败，记录失败日志
                if (!future.isSuccess()) {
                    logger.error("ErrSend:{},cause by", finalmsg, future.cause());
                } else {
                    logger.debug("Send:{}", finalmsg);
                }
            }
        });
    }

}
