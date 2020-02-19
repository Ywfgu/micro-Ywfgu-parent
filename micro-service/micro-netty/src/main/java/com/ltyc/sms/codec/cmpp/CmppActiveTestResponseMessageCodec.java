package com.ltyc.sms.codec.cmpp;

import com.ltyc.sms.entity.cmpp30.msg.CmppActiveTestResponseMessage;
import com.ltyc.sms.entity.cmpp30.msg.Message;
import com.ltyc.sms.entity.cmpp30.packet.Cmpp30ActiveTestResponse;
import com.ltyc.sms.entity.cmpp30.packet.CmppPacketType;
import com.ltyc.sms.entity.PacketType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

import static com.ltyc.sms.common.utils.NettyByteBufUtil.toArray;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class CmppActiveTestResponseMessageCodec extends MessageToMessageCodec<Message, CmppActiveTestResponseMessage> {
    private PacketType packetType;

    public CmppActiveTestResponseMessageCodec() {
        this(CmppPacketType.CMPPACTIVETESTRESPONSE);
    }

    public CmppActiveTestResponseMessageCodec(PacketType packetType) {
        this.packetType = packetType;
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        long commandId = ((Long) msg.getHeader().getCommandId()).longValue();
        if (packetType.getCommandId() != commandId)
        {
            //不解析，交给下一个codec
            out.add(msg);
            return;
        }

        CmppActiveTestResponseMessage responseMessage = new CmppActiveTestResponseMessage(msg.getHeader());
        ByteBuf bodyBuffer = Unpooled.wrappedBuffer(msg.getBodyBuffer());

        //甘肃测试环境回包缺少reserved字段，这里要容错
        if(bodyBuffer.readableBytes()>0) {
            responseMessage.setReserved(bodyBuffer.readByte());
        }

        ReferenceCountUtil.release(bodyBuffer);
        out.add(responseMessage);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, CmppActiveTestResponseMessage msg, List<Object> out) throws Exception {

        ByteBuf bodyBuffer = ctx.alloc().buffer(Cmpp30ActiveTestResponse.RESERVED.getBodyLength());
        bodyBuffer.writeByte(msg.getReserved());
        msg.setBodyBuffer(toArray(bodyBuffer,bodyBuffer.readableBytes()));
        msg.getHeader().setBodyLength(msg.getBodyBuffer().length);
        ReferenceCountUtil.release(bodyBuffer);
        out.add(msg);
    }

}
