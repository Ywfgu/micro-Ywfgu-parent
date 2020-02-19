package com.ltyc.sms.codec.cmpp;

import com.ltyc.sms.common.NotSupportedException;
import com.ltyc.sms.entity.cmpp20.packet.Cmpp20ConnectResponse;
import com.ltyc.sms.entity.cmpp30.msg.CmppConnectResponseMessage;
import com.ltyc.sms.entity.cmpp30.msg.Message;
import com.ltyc.sms.entity.cmpp30.packet.Cmpp30ConnectResponse;
import com.ltyc.sms.entity.cmpp30.packet.CmppPacketType;
import com.ltyc.sms.entity.PacketType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.ltyc.sms.common.utils.NettyByteBufUtil.toArray;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class CmppConnectResponseMessageCodec extends MessageToMessageCodec<Message, CmppConnectResponseMessage> {
    private PacketType packetType;
    private final Logger logger = LoggerFactory.getLogger(CmppConnectResponseMessageCodec.class);

    /**
     *
     */
    public CmppConnectResponseMessageCodec() {
        this(CmppPacketType.CMPPCONNECTRESPONSE);
    }

    public CmppConnectResponseMessageCodec(PacketType packetType) {
        this.packetType = packetType;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        long commandId = ((Long) msg.getHeader().getCommandId()).longValue();
        if (commandId != packetType.getCommandId())
        {
            //不解析，交给下一个codec
            out.add(msg);
            return;
        }
        CmppConnectResponseMessage responseMessage = new CmppConnectResponseMessage(msg.getHeader());
        byte[] body = msg.getBodyBuffer();
        if(body.length == 21){
            ByteBuf bodyBuffer = Unpooled.wrappedBuffer(msg.getBodyBuffer());

            responseMessage.setStatus(bodyBuffer.readUnsignedInt());
            responseMessage.setAuthenticatorISMG(toArray(bodyBuffer, Cmpp30ConnectResponse.AUTHENTICATORISMG.getLength()));
            responseMessage.setVersion(bodyBuffer.readUnsignedByte());

            ReferenceCountUtil.release(bodyBuffer);
            out.add(responseMessage);
        }else{
            if(body.length == 18) {
                ByteBuf bodyBuffer = Unpooled.wrappedBuffer(body);
                responseMessage.setStatus(bodyBuffer.readUnsignedByte());
                responseMessage.setAuthenticatorISMG(toArray(bodyBuffer, Cmpp20ConnectResponse.AUTHENTICATORISMG.getLength()));
                responseMessage.setVersion(bodyBuffer.readUnsignedByte());
                ReferenceCountUtil.release(bodyBuffer);
                out.add(responseMessage);
                logger.warn("error CmppConnectResponseMessage body length. dump:{}", ByteBufUtil.hexDump(body));
            }else {
                throw new NotSupportedException("error cmpp CmppConnectResponseMessage data .");
            }
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, CmppConnectResponseMessage msg, List<Object> out) throws Exception {

        ByteBuf bodyBuffer = ctx.alloc().buffer(Cmpp30ConnectResponse.AUTHENTICATORISMG.getBodyLength());

        bodyBuffer.writeInt((int) msg.getStatus());
        bodyBuffer.writeBytes(msg.getAuthenticatorISMG());
        bodyBuffer.writeByte(msg.getVersion());

        msg.setBodyBuffer(toArray(bodyBuffer,bodyBuffer.readableBytes()));
        msg.getHeader().setBodyLength(msg.getBodyBuffer().length);
        ReferenceCountUtil.release(bodyBuffer);
        out.add(msg);

    }

}
