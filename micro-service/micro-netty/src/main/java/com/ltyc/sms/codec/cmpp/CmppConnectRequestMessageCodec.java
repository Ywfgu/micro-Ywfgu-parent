package com.ltyc.sms.codec.cmpp;

import com.ltyc.sms.common.GlobalConstance;
import com.ltyc.sms.common.utils.CMPPCommonUtil;
import com.ltyc.sms.entity.cmpp30.CmppConnectRequestMessage;
import com.ltyc.sms.entity.cmpp30.msg.Message;
import com.ltyc.sms.entity.cmpp30.packet.Cmpp30ConnectRequest;
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
public class CmppConnectRequestMessageCodec extends MessageToMessageCodec<Message, CmppConnectRequestMessage> {
    private PacketType packetType;

    public CmppConnectRequestMessageCodec() {
        this(CmppPacketType.CMPPCONNECTREQUEST);
    }

    public CmppConnectRequestMessageCodec(PacketType packetType) {
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
        CmppConnectRequestMessage requestMessage = new CmppConnectRequestMessage(msg.getHeader());

        ByteBuf bodyBuffer = Unpooled.wrappedBuffer(msg.getBodyBuffer());
        requestMessage.setSourceAddr(bodyBuffer.readCharSequence(Cmpp30ConnectRequest.SOURCEADDR.getLength(),GlobalConstance.defaultTransportCharset).toString().trim());

        requestMessage.setAuthenticatorSource(toArray(bodyBuffer,Cmpp30ConnectRequest.AUTHENTICATORSOURCE.getLength()));

        requestMessage.setVersion(bodyBuffer.readUnsignedByte());
        requestMessage.setTimestamp(bodyBuffer.readUnsignedInt());

        ReferenceCountUtil.release(bodyBuffer);
        out.add(requestMessage);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, CmppConnectRequestMessage msg, List<Object> out) throws Exception {

        ByteBuf bodyBuffer =  ctx.alloc().buffer(Cmpp30ConnectRequest.AUTHENTICATORSOURCE.getBodyLength());

        bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(msg.getSourceAddr().getBytes(GlobalConstance.defaultTransportCharset),
                Cmpp30ConnectRequest.SOURCEADDR.getLength(), 0));
        bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(msg.getAuthenticatorSource(),Cmpp30ConnectRequest.AUTHENTICATORSOURCE.getLength(),0));
        bodyBuffer.writeByte(msg.getVersion());
        bodyBuffer.writeInt((int) msg.getTimestamp());

        msg.setBodyBuffer(toArray(bodyBuffer,bodyBuffer.readableBytes()));
        msg.getHeader().setBodyLength(msg.getBodyBuffer().length);
        ReferenceCountUtil.release(bodyBuffer);
        out.add(msg);
    }

}
