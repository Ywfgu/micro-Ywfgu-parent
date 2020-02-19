package com.ltyc.sms.codec.cmpp;

import com.ltyc.sms.common.GlobalConstance;
import com.ltyc.sms.entity.cmpp30.msg.CmppActiveTestRequestMessage;
import com.ltyc.sms.entity.cmpp30.msg.Message;
import com.ltyc.sms.entity.cmpp30.packet.CmppPacketType;
import com.ltyc.sms.entity.PacketType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class CmppActiveTestRequestMessageCodec extends MessageToMessageCodec<Message, CmppActiveTestRequestMessage> {
    private PacketType packetType;

    public CmppActiveTestRequestMessageCodec() {
        this(CmppPacketType.CMPPACTIVETESTREQUEST);
    }

    public CmppActiveTestRequestMessageCodec(PacketType packetType) {
        this.packetType = packetType;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        long commandId = ((Long) msg.getHeader().getCommandId()).longValue();
        if (packetType.getCommandId() != commandId) {
            //不解析，交给下一个codec
            out.add(msg);
            return;
        }

        CmppActiveTestRequestMessage requestMessage = new CmppActiveTestRequestMessage(msg.getHeader());
        out.add(requestMessage);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, CmppActiveTestRequestMessage msg, List<Object> out) throws Exception {
        msg.setBodyBuffer(GlobalConstance.emptyBytes);
        msg.getHeader().setBodyLength(msg.getBodyBuffer().length);
        out.add(msg);
    }

}
