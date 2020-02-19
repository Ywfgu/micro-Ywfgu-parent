package com.ltyc.sms.session;

import com.ltyc.sms.connect.manager.EndpointEntity;
import com.ltyc.sms.entity.BaseMessage;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author guht
 * @version 1.0
 * @Description 消息持久化，在消息未收到response时都处于重发介质中，在重发的存储介质中会有定时任务一直循环遍历需要重发的消息
 * @create 2020/2/6
 */
public abstract class AbstractSessionStateManager<K, T extends BaseMessage> extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(AbstractSessionStateManager.class);

    /**
     * 连接流量统计
     **/
    private long msgReadCount = 0;
    private long msgWriteCount = 0;
    private EndpointEntity entity;

    public long getReadCount() {
        return msgReadCount;
    }

    public long getWriteCount() {
        return msgWriteCount;
    }

    public EndpointEntity getEntity() {
        return entity;
    }

    /**
     * @param entity Session关联的端口
     */
    public AbstractSessionStateManager(EndpointEntity entity) {
        this.entity = entity;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object message, ChannelPromise promise) throws Exception {

        if (message instanceof BaseMessage) {
            BaseMessage msg = (BaseMessage) message;

            if (msg.isRequest()) {
                // 发送Request消息时先持久化到存储介质，在直接发送
                //TODO 存储到介质
                msgWriteCount++;
                ctx.write(msg, promise);
            } else {
                // 发送Response消息时直接发送
                ctx.write(msg, promise);
            }
        } else {
            // 不是Message消息时直接发送
            ctx.write(message, promise);
        }
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        msgReadCount++;
        if (msg instanceof BaseMessage) {
            // 如果是resp，从存储介质中删除（取消 消息重发）
            if (((T) msg).isResponse()) {
                //TODO 从存储介质中删除
            }
        }
        ctx.fireChannelRead(msg);
    }

    protected abstract K getSequenceId(T msg);

}
