package com.ltyc.sms.codec.cmpp;

import com.ltyc.sms.common.utils.DefaultMsgIdUtil;
import com.ltyc.sms.entity.PacketType;
import com.ltyc.sms.entity.cmpp30.msg.CmppSubmitResponseMessage;
import com.ltyc.sms.entity.cmpp30.msg.Message;
import com.ltyc.sms.entity.cmpp30.packet.Cmpp30SubmitResponse;
import com.ltyc.sms.entity.cmpp30.packet.CmppPacketType;
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
public class CmppSubmitResponseMessageCodec extends MessageToMessageCodec<Message, CmppSubmitResponseMessage> {
    private PacketType packetType;

    /**
     *
     */
    public CmppSubmitResponseMessageCodec() {
        this(CmppPacketType.CMPPSUBMITRESPONSE);
    }

    public CmppSubmitResponseMessageCodec(PacketType packetType) {
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

        CmppSubmitResponseMessage responseMessage = new CmppSubmitResponseMessage(msg.getHeader());

        ByteBuf bodyBuffer = Unpooled.wrappedBuffer(msg.getBodyBuffer());

        responseMessage.setMsgId(DefaultMsgIdUtil.bytes2MsgId(toArray(bodyBuffer,Cmpp30SubmitResponse.MSGID.getLength())));
        responseMessage.setResult(bodyBuffer.readUnsignedInt());
        ReferenceCountUtil.release(bodyBuffer);

        out.add(responseMessage);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, CmppSubmitResponseMessage msg, List<Object> out) throws Exception {
        ByteBuf bodyBuffer = ctx.alloc().buffer(Cmpp30SubmitResponse.RESULT.getBodyLength());

        bodyBuffer.writeBytes(DefaultMsgIdUtil.msgId2Bytes(msg.getMsgId()));
        bodyBuffer.writeInt((int) msg.getResult());
        msg.setBodyBuffer(toArray(bodyBuffer,bodyBuffer.readableBytes()));
        msg.getHeader().setBodyLength(msg.getBodyBuffer().length);
        ReferenceCountUtil.release(bodyBuffer);
        out.add(msg);
    }
}
