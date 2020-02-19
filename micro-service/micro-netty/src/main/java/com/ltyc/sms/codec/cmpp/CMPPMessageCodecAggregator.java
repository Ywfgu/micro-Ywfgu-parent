package com.ltyc.sms.codec.cmpp;

import com.ltyc.sms.entity.PacketType;
import com.ltyc.sms.entity.cmpp30.msg.Message;
import com.ltyc.sms.entity.cmpp30.packet.CmppPacketType;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
@Sharable
public class CMPPMessageCodecAggregator extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(CMPPMessageCodecAggregator.class);

    private static class CMPPMessageCodecAggregatorHolder {
        private final static CMPPMessageCodecAggregator instance = new CMPPMessageCodecAggregator();
    }

    private ConcurrentHashMap<Long, MessageToMessageCodec> codecMap = new ConcurrentHashMap<Long, MessageToMessageCodec>();

    private CMPPMessageCodecAggregator() {
        for (PacketType packetType : CmppPacketType.values()) {
            codecMap.put(packetType.getCommandId(), packetType.getCodec());
        }
    }

    public static CMPPMessageCodecAggregator getInstance() {
        return CMPPMessageCodecAggregatorHolder.instance;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        long commandId = (Long) ((Message) msg).getHeader().getCommandId();
        MessageToMessageCodec codec = codecMap.get(commandId);
        codec.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        try {
            long commandId = (Long) ((Message) msg).getHeader().getCommandId();
            MessageToMessageCodec codec = codecMap.get(commandId);
            codec.write(ctx, msg, promise);
        } catch (Exception ex) {
            promise.tryFailure(ex);
        }
    }
}
